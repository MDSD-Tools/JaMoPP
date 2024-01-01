package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToConcreteClassifierConverterWithExtraInfo;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingInfoToConcreteClassifierConverterImpl implements ToConcreteClassifierConverterWithExtraInfo {

	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtTResolverUtility;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final Converter<IVariableBinding, EnumConstant> bindingToEnumConstantConverter;
	private final Converter<IMethodBinding, Method> bindingToMethodConverter;
	private final Converter<IMethodBinding, Constructor> bindingToConstructorConverter;
	private final Converter<IVariableBinding, Field> bindingToFieldConverter;
	private final Converter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter;

	@Inject
	BindingInfoToConcreteClassifierConverterImpl(UtilNamedElement utilNamedElement,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter,
			JdtResolver jdtTResolverUtility, Converter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter,
			Converter<IMethodBinding, Method> bindingToMethodConverter,
			Converter<IVariableBinding, Field> bindingToFieldConverter,
			Converter<IVariableBinding, EnumConstant> bindingToEnumConstantConverter,
			Converter<IMethodBinding, Constructor> bindingToConstructorConverter,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
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

	@Override
	public ConcreteClassifier convert(ITypeBinding binding, boolean extractAdditionalInformation) {
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
					result.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
				for (ITypeBinding typeBind : binding.getTypeParameters()) {
					result.getTypeParameters().add(bindingToTypeParameterConverter.convert(typeBind));
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
				member = bindingToFieldConverter.convert(varBind);
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
			for (IMethodBinding methBind : binding.getDeclaredMethods()) {
				if (methBind.isDefaultConstructor()) {
					continue;
				}
				if (methBind.isConstructor()) {
					member = bindingToConstructorConverter.convert(methBind);
				} else {
					member = bindingToMethodConverter.convert(methBind);
				}
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
			for (ITypeBinding typeBind : binding.getDeclaredTypes()) {
				member = convert(typeBind, true);
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
		tools.mdsd.jamopp.model.java.classifiers.Enumeration resultEnum = jdtTResolverUtility.getEnumeration(binding);
		if (resultEnum.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultEnum.getImplements().addAll(toTypeReferencesConverter.convert(typeBind));
				}
				if (extractAdditionalInformation) {
					for (IVariableBinding varBind : binding.getDeclaredFields()) {
						if (varBind.isEnumConstant()) {
							resultEnum.getConstants().add(bindingToEnumConstantConverter.convert(varBind));
						}
					}
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		return resultEnum;
	}

	private ConcreteClassifier handleInterface(ITypeBinding binding) {
		Interface resultInterface = jdtTResolverUtility.getInterface(binding);
		if (resultInterface.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultInterface.getExtends().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		return resultInterface;
	}

	private ConcreteClassifier handleClass(ITypeBinding binding) {
		tools.mdsd.jamopp.model.java.classifiers.Class resultClass = jdtTResolverUtility.getClass(binding);
		if (resultClass.eContainer() == null) {
			try {
				if (binding.getSuperclass() != null) {
					resultClass.setExtends(toTypeReferencesConverter.convert(binding.getSuperclass()).get(0));
				}
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultClass.getImplements().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		return resultClass;
	}

}
