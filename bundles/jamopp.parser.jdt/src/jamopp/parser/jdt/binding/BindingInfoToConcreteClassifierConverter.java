package jamopp.parser.jdt.binding;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.members.Member;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToModifiersConverter;
import jamopp.parser.jdt.converter.ToTypeReferencesConverter;
import jamopp.parser.jdt.other.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilNamedElement;

public class BindingInfoToConcreteClassifierConverter {

	private final UtilNamedElement utilNamedElement;
	private final ToTypeReferencesConverter toTypeReferencesConverter;
	private final UtilJdtResolver jdtTResolverUtility;
	private final BindingToEnumConstantConverter bindingToEnumConstantConverter;
	private final BindingToMethodConverter bindingToMethodConverter;
	private final BindingToConstructorConverter bindingToConstructorConverter;
	private final BindingToFieldConverter bindingToFieldConverter;
	private final BindingToTypeParameterConverter bindingToTypeParameterConverter;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;
	private final ToModifiersConverter toModifiersConverter;

	@Inject
	BindingInfoToConcreteClassifierConverter(UtilNamedElement utilNamedElement,
			ToTypeReferencesConverter toTypeReferencesConverter, ToModifiersConverter toModifiersConverter,
			UtilJdtResolver jdtTResolverUtility, BindingToTypeParameterConverter bindingToTypeParameterConverter,
			BindingToMethodConverter bindingToMethodConverter, BindingToFieldConverter bindingToFieldConverter,
			BindingToEnumConstantConverter bindingToEnumConstantConverter,
			BindingToConstructorConverter bindingToConstructorConverter,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToEnumConstantConverter = bindingToEnumConstantConverter;
		this.bindingToMethodConverter = bindingToMethodConverter;
		this.bindingToConstructorConverter = bindingToConstructorConverter;
		this.bindingToFieldConverter = bindingToFieldConverter;
		this.bindingToTypeParameterConverter = bindingToTypeParameterConverter;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	public ConcreteClassifier convertToConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation) {
		binding = binding.getTypeDeclaration();

		ConcreteClassifier result = getConcreteClassifier(binding, extractAdditionalInformation);

		result.setPackage(jdtTResolverUtility.getPackage(binding.getPackage()));
		if (result.eContainer() == null) {
			handleEmptyContainer(binding, extractAdditionalInformation, result);
		}
		if (extractAdditionalInformation) {
			extractAdditionalInformation(binding, result);
		}
		return result;
	}

	private void handleEmptyContainer(ITypeBinding binding, boolean extractAdditionalInformation,
			ConcreteClassifier result) {
		if (extractAdditionalInformation) {
			try {
				for (IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotationsAndModifiers()
							.add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
				}
				for (ITypeBinding typeBind : binding.getTypeParameters()) {
					result.getTypeParameters().add(bindingToTypeParameterConverter.convertToTypeParameter(typeBind));
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		result.getAnnotationsAndModifiers().addAll(toModifiersConverter.convert(binding.getModifiers()));
		utilNamedElement.convertToNameAndSet(binding, result);
	}

	private void extractAdditionalInformation(ITypeBinding binding, ConcreteClassifier result) {
		try {
			Member member;
			for (IVariableBinding varBind : binding.getDeclaredFields()) {
				if (varBind.isEnumConstant()) {
					continue;
				}
				member = bindingToFieldConverter.convertToField(varBind);
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
			for (IMethodBinding methBind : binding.getDeclaredMethods()) {
				if (methBind.isDefaultConstructor()) {
					continue;
				}
				if (methBind.isConstructor()) {
					member = bindingToConstructorConverter.convertToConstructor(methBind);
				} else {
					member = bindingToMethodConverter.convertToMethod(methBind);
				}
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
			for (ITypeBinding typeBind : binding.getDeclaredTypes()) {
				member = convertToConcreteClassifier(typeBind, true);
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
		} catch (AbortCompilation e) {
			// Ignore
		}
	}

	private ConcreteClassifier getConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation) {
		ConcreteClassifier result = null;
		if (binding.isAnnotation()) {
			result = jdtTResolverUtility.getAnnotation(binding);
		} else if (binding.isClass()) {
			result = handleClass(binding);
		} else if (binding.isInterface()) {
			result = handleInterface(binding);
		} else {
			result = handleElse(binding, extractAdditionalInformation);
		}
		return result;
	}

	private ConcreteClassifier handleElse(ITypeBinding binding, boolean extractAdditionalInformation) {
		ConcreteClassifier result;
		org.emftext.language.java.classifiers.Enumeration resultEnum = jdtTResolverUtility.getEnumeration(binding);
		if (resultEnum.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultEnum.getImplements().addAll(toTypeReferencesConverter.convert(typeBind));
				}
				if (extractAdditionalInformation) {
					for (IVariableBinding varBind : binding.getDeclaredFields()) {
						if (varBind.isEnumConstant()) {
							resultEnum.getConstants()
									.add(bindingToEnumConstantConverter.convertToEnumConstant(varBind));
						}
					}
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		result = resultEnum;
		return result;
	}

	private ConcreteClassifier handleInterface(ITypeBinding binding) {
		ConcreteClassifier result;
		Interface resultInterface = jdtTResolverUtility.getInterface(binding);
		if (resultInterface.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultInterface.getExtends().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
			}
		}
		result = resultInterface;
		return result;
	}

	private ConcreteClassifier handleClass(ITypeBinding binding) {
		ConcreteClassifier result;
		org.emftext.language.java.classifiers.Class resultClass = jdtTResolverUtility.getClass(binding);
		if (resultClass.eContainer() == null) {
			try {
				if (binding.getSuperclass() != null) {
					resultClass.setExtends(toTypeReferencesConverter.convert(binding.getSuperclass()).get(0));
				}
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultClass.getImplements().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
			}
		}
		result = resultClass;
		return result;
	}

}
