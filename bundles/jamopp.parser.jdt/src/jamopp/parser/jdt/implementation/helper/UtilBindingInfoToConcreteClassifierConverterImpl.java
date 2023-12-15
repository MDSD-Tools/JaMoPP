package jamopp.parser.jdt.implementation.helper;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilBindingInfoToConcreteClassifierConverter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

@SuppressWarnings("restriction")
public class UtilBindingInfoToConcreteClassifierConverterImpl implements UtilBindingInfoToConcreteClassifierConverter {

	private final Converter<IAnnotationBinding, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<IMethodBinding, Constructor> toConstructorConverter;
	private final Converter<IVariableBinding, EnumConstant> toEnumConstantConverter;
	private final Converter<IVariableBinding, Field> toFieldConverter;
	private final Converter<IMethodBinding, Method> toMethodConverter;
	private final Converter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter;
	private final Converter<ITypeBinding, TypeParameter> toTypeParameterConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final UtilJdtResolver utilJdtResolver;
	private final UtilNamedElement utilNamedElement;

	@Inject
	UtilBindingInfoToConcreteClassifierConverterImpl(UtilNamedElement utilNamedElement,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			Converter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter,
			UtilJdtResolver jdtTResolverUtility, Converter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter,
			Converter<IMethodBinding, Method> bindingToMethodConverter,
			Converter<IVariableBinding, Field> bindingToFieldConverter,
			Converter<IVariableBinding, EnumConstant> bindingToEnumConstantConverter,
			Converter<IMethodBinding, Constructor> bindingToConstructorConverter,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.utilJdtResolver = jdtTResolverUtility;
		this.toEnumConstantConverter = bindingToEnumConstantConverter;
		this.toMethodConverter = bindingToMethodConverter;
		this.toConstructorConverter = bindingToConstructorConverter;
		this.toFieldConverter = bindingToFieldConverter;
		this.toTypeParameterConverter = bindingToTypeParameterConverter;
		this.toAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	@Override
	public ConcreteClassifier convertToConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation) {
		binding = binding.getTypeDeclaration();

		var result = getConcreteClassifier(binding, extractAdditionalInformation);

		result.setPackage(this.utilJdtResolver.getPackage(binding.getPackage()));
		if (result.eContainer() == null) {
			handleEmptyContainer(binding, extractAdditionalInformation, result);
		}
		if (extractAdditionalInformation) {
			extractAdditionalInformation(binding, result);
		}
		return result;
	}

	private void extractAdditionalInformation(ITypeBinding binding, ConcreteClassifier result) {
		try {
			Member member;
			for (IVariableBinding varBind : binding.getDeclaredFields()) {
				if (varBind.isEnumConstant()) {
					continue;
				}
				member = this.toFieldConverter.convert(varBind);
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
			for (IMethodBinding methBind : binding.getDeclaredMethods()) {
				if (methBind.isDefaultConstructor()) {
					continue;
				}
				if (methBind.isConstructor()) {
					member = this.toConstructorConverter.convert(methBind);
				} else {
					member = this.toMethodConverter.convert(methBind);
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
			result = this.utilJdtResolver.getAnnotation(binding);
		} else if (binding.isClass()) {
			result = handleClass(binding);
		} else if (binding.isInterface()) {
			result = handleInterface(binding);
		} else {
			result = handleElse(binding, extractAdditionalInformation);
		}
		return result;
	}

	private ConcreteClassifier handleClass(ITypeBinding binding) {
		var resultClass = this.utilJdtResolver.getClass(binding);
		if (resultClass.eContainer() == null) {
			try {
				if (binding.getSuperclass() != null) {
					resultClass.setExtends(this.toTypeReferencesConverter.convert(binding.getSuperclass()).get(0));
				}
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultClass.getImplements().addAll(this.toTypeReferencesConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
			}
		}
		return resultClass;
	}

	private ConcreteClassifier handleElse(ITypeBinding binding, boolean extractAdditionalInformation) {
		var resultEnum = this.utilJdtResolver.getEnumeration(binding);
		if (resultEnum.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultEnum.getImplements().addAll(this.toTypeReferencesConverter.convert(typeBind));
				}
				if (extractAdditionalInformation) {
					for (IVariableBinding varBind : binding.getDeclaredFields()) {
						if (varBind.isEnumConstant()) {
							resultEnum.getConstants().add(this.toEnumConstantConverter.convert(varBind));
						}
					}
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		return resultEnum;
	}

	private void handleEmptyContainer(ITypeBinding binding, boolean extractAdditionalInformation,
			ConcreteClassifier result) {
		if (extractAdditionalInformation) {
			try {
				for (IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotationsAndModifiers().add(this.toAnnotationInstanceConverter.convert(annotBind));
				}
				for (ITypeBinding typeBind : binding.getTypeParameters()) {
					result.getTypeParameters().add(this.toTypeParameterConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		result.getAnnotationsAndModifiers().addAll(this.toModifiersConverter.convert(binding.getModifiers()));
		this.utilNamedElement.convertToNameAndSet(binding, result);
	}

	private ConcreteClassifier handleInterface(ITypeBinding binding) {
		var resultInterface = this.utilJdtResolver.getInterface(binding);
		if (resultInterface.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultInterface.getExtends().addAll(this.toTypeReferencesConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
			}
		}
		return resultInterface;
	}

}
