package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToConstructorConverter implements Converter<IMethodBinding, Constructor> {

	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final JdtResolver jdtTResolverUtility;
	private final UtilArrays utilJdtBindingConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final Converter<ITypeBinding, NamespaceClassifierReference> bindingToNamespaceClassifierReferenceConverter;
	private final Converter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter;
	private final Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	public BindingToConstructorConverter(final UtilArrays utilJdtBindingConverter,
			final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			final Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter,
			final ParametersFactory parametersFactory, final LiteralsFactory literalsFactory,
			final JdtResolver jdtTResolverUtility,
			final Converter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter,
			final Converter<ITypeBinding, NamespaceClassifierReference> bindingToNamespaceClassifierReferenceConverter,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.literalsFactory = literalsFactory;
		this.parametersFactory = parametersFactory;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.bindingToNamespaceClassifierReferenceConverter = bindingToNamespaceClassifierReferenceConverter;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.bindingToTypeParameterConverter = bindingToTypeParameterConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	@Override
	public Constructor convert(final IMethodBinding binding) {
		final Constructor result = jdtTResolverUtility.getConstructor(binding);
		if (result.eContainer() == null) {
			addAnnotationsAndModifiers(binding, result);
			result.setName(binding.getName());
			addTypeParameters(binding, result);
			addParameters(binding, result);
			addExceptions(binding, result);

		}
		return result;
	}

	private void addExceptions(final IMethodBinding binding, final Constructor result) {
		for (final ITypeBinding typeBind : binding.getExceptionTypes()) {
			result.getExceptions().add(bindingToNamespaceClassifierReferenceConverter.convert(typeBind));
		}
	}

	private void addParameters(final IMethodBinding binding, final Constructor result) {
		if (binding.getDeclaredReceiverType() != null) {
			final ReceiverParameter param = parametersFactory.createReceiverParameter();
			param.setName("");
			param.setTypeReference(toTypeReferencesConverter.convert(binding.getDeclaredReceiverType()).get(0));
			param.setOuterTypeReference(param.getTypeReference());
			param.setThisReference(literalsFactory.createThis());
			result.getParameters().add(param);
		}
		for (int index = 0; index < binding.getParameterTypes().length; index++) {
			final ITypeBinding typeBind = binding.getParameterTypes()[index];
			Parameter param;
			if (binding.isVarargs() && index == binding.getParameterTypes().length - 1) {
				param = parametersFactory.createVariableLengthParameter();
			} else {
				param = parametersFactory.createOrdinaryParameter();
			}
			param.setName("param" + index);
			param.setTypeReference(toTypeReferencesConverter.convert(typeBind).get(0));
			utilJdtBindingConverter.convertToArrayDimensionsAndSet(typeBind, param);
			final IAnnotationBinding[] binds = binding.getParameterAnnotations(index);
			try {
				for (final IAnnotationBinding annotBind : binds) {
					param.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
			result.getParameters().add(param);
		}
	}

	private void addTypeParameters(final IMethodBinding binding, final Constructor result) {
		try {
			for (final ITypeBinding typeBind : binding.getTypeParameters()) {
				result.getTypeParameters().add(bindingToTypeParameterConverter.convert(typeBind));
			}
		} catch (final AbortCompilation e) {
			// Ignore
		}
	}

	private void addAnnotationsAndModifiers(final IMethodBinding binding, final Constructor result) {
		result.getAnnotationsAndModifiers().addAll(toModifiersConverter.convert(binding.getModifiers()));
		try {
			for (final IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (final AbortCompilation e) {
			// Ignore
		}
	}

}
