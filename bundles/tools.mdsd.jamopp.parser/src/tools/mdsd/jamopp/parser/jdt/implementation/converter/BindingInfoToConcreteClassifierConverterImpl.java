package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

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
	public BindingInfoToConcreteClassifierConverterImpl(final UtilNamedElement utilNamedElement,
			final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			final Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter,
			final JdtResolver jdtTResolverUtility,
			final Converter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter,
			final Converter<IMethodBinding, Method> bindingToMethodConverter,
			final Converter<IVariableBinding, Field> bindingToFieldConverter,
			final Converter<IVariableBinding, EnumConstant> bindingToEnumConstantConverter,
			final Converter<IMethodBinding, Constructor> bindingToConstructorConverter,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
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
	public ConcreteClassifier convert(final ITypeBinding binding, final boolean extractAdditionalInformation) {
		final ITypeBinding typeDeclaration = binding.getTypeDeclaration();

		final ConcreteClassifier result = getConcreteClassifier(typeDeclaration, extractAdditionalInformation);

		result.setPackage(jdtTResolverUtility.getPackage(typeDeclaration.getPackage()));
		if (result.eContainer() == null) {
			handleEmptyContainer(typeDeclaration, extractAdditionalInformation, result);
		}
		if (extractAdditionalInformation) {
			extractAdditionalInformation(typeDeclaration, result);
		}
		return result;
	}

	private void handleEmptyContainer(final ITypeBinding binding, final boolean extractAdditionalInformation,
			final ConcreteClassifier result) {
		if (extractAdditionalInformation) {
			try {
				for (final IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
				for (final ITypeBinding typeBind : binding.getTypeParameters()) {
					result.getTypeParameters().add(bindingToTypeParameterConverter.convert(typeBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
		}
		result.getAnnotationsAndModifiers().addAll(toModifiersConverter.convert(binding.getModifiers()));
		utilNamedElement.convertToNameAndSet(binding, result);
	}

	private void extractAdditionalInformation(final ITypeBinding binding, final ConcreteClassifier result) {
		try {
			addFields(binding, result);
			addMethods(binding, result);
			addTypes(binding, result);
		} catch (final AbortCompilation ignore) {
			// Ignore
		}
	}

	private void addTypes(final ITypeBinding binding, final ConcreteClassifier result) {
		Member member;
		for (final ITypeBinding typeBind : binding.getDeclaredTypes()) {
			member = convert(typeBind, true);
			if (!result.getMembers().contains(member)) {
				result.getMembers().add(member);
			}
		}
	}

	private void addFields(final ITypeBinding binding, final ConcreteClassifier result) {
		for (final IVariableBinding varBind : binding.getDeclaredFields()) {
			if (varBind.isEnumConstant()) {
				continue;
			}
			final Member member = bindingToFieldConverter.convert(varBind);
			if (!result.getMembers().contains(member)) {
				result.getMembers().add(member);
			}
		}
	}

	private void addMethods(final ITypeBinding binding, final ConcreteClassifier result) {
		for (final IMethodBinding methBind : binding.getDeclaredMethods()) {
			if (methBind.isDefaultConstructor()) {
				continue;
			}
			Member member;
			if (methBind.isConstructor()) {
				member = bindingToConstructorConverter.convert(methBind);
			} else {
				member = bindingToMethodConverter.convert(methBind);
			}
			if (!result.getMembers().contains(member)) {
				result.getMembers().add(member);
			}
		}
	}

	private ConcreteClassifier getConcreteClassifier(final ITypeBinding binding,
			final boolean extractAdditionalInformation) {
		ConcreteClassifier result;
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

	private ConcreteClassifier handleElse(final ITypeBinding binding, final boolean extractAdditionalInformation) {
		final tools.mdsd.jamopp.model.java.classifiers.Enumeration resultEnum = jdtTResolverUtility
				.getEnumeration(binding);
		if (resultEnum.eContainer() == null) {
			try {
				for (final ITypeBinding typeBind : binding.getInterfaces()) {
					resultEnum.getImplements().addAll(toTypeReferencesConverter.convert(typeBind));
				}
				if (extractAdditionalInformation) {
					for (final IVariableBinding varBind : binding.getDeclaredFields()) {
						if (varBind.isEnumConstant()) {
							resultEnum.getConstants().add(bindingToEnumConstantConverter.convert(varBind));
						}
					}
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
		}
		return resultEnum;
	}

	private ConcreteClassifier handleInterface(final ITypeBinding binding) {
		final Interface resultInterface = jdtTResolverUtility.getInterface(binding);
		if (resultInterface.eContainer() == null) {
			try {
				for (final ITypeBinding typeBind : binding.getInterfaces()) {
					resultInterface.getExtends().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
		}
		return resultInterface;
	}

	private ConcreteClassifier handleClass(final ITypeBinding binding) {
		final tools.mdsd.jamopp.model.java.classifiers.Class resultClass = jdtTResolverUtility.getClass(binding);
		if (resultClass.eContainer() == null) {
			try {
				if (binding.getSuperclass() != null) {
					resultClass.setExtends(toTypeReferencesConverter.convert(binding.getSuperclass()).get(0));
				}
				for (final ITypeBinding typeBind : binding.getInterfaces()) {
					resultClass.getImplements().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
		}
		return resultClass;
	}

}
