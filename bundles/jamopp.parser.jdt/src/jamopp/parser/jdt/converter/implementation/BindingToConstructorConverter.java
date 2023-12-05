package jamopp.parser.jdt.converter.implementation;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilArrays;

@SuppressWarnings("restriction")
public class BindingToConstructorConverter implements ToConverter<IMethodBinding, Constructor> {

	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final UtilJdtResolver jdtTResolverUtility;
	private final UtilArrays utilJdtBindingConverter;
	private final ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final ToConverter<ITypeBinding, NamespaceClassifierReference> bindingToNamespaceClassifierReferenceConverter;
	private final ToConverter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter;
	private final ToConverter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter;
	private final ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	BindingToConstructorConverter(UtilArrays utilJdtBindingConverter,
			ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			ToConverter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter,
			ParametersFactory parametersFactory, LiteralsFactory literalsFactory, UtilJdtResolver jdtTResolverUtility,
			ToConverter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter,
			ToConverter<ITypeBinding, NamespaceClassifierReference> bindingToNamespaceClassifierReferenceConverter,
			ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
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
	public Constructor convert(IMethodBinding binding) {
		Constructor result = jdtTResolverUtility.getConstructor(binding);
		if (result.eContainer() != null) {
			return result;
		}
		result.getAnnotationsAndModifiers().addAll(toModifiersConverter.convert(binding.getModifiers()));
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		try {
			for (ITypeBinding typeBind : binding.getTypeParameters()) {
				result.getTypeParameters().add(bindingToTypeParameterConverter.convert(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.getDeclaredReceiverType() != null) {
			ReceiverParameter param = parametersFactory.createReceiverParameter();
			param.setName("");
			param.setTypeReference(toTypeReferencesConverter.convert(binding.getDeclaredReceiverType()).get(0));
			param.setOuterTypeReference(param.getTypeReference());
			param.setThisReference(literalsFactory.createThis());
			result.getParameters().add(param);
		}
		for (int index = 0; index < binding.getParameterTypes().length; index++) {
			ITypeBinding typeBind = binding.getParameterTypes()[index];
			Parameter param;
			if (binding.isVarargs() && index == binding.getParameterTypes().length - 1) {
				param = parametersFactory.createVariableLengthParameter();
			} else {
				param = parametersFactory.createOrdinaryParameter();
			}
			param.setName("param" + index);
			param.setTypeReference(toTypeReferencesConverter.convert(typeBind).get(0));
			utilJdtBindingConverter.convertToArrayDimensionsAndSet(typeBind, param);
			IAnnotationBinding[] binds = binding.getParameterAnnotations(index);
			try {
				for (IAnnotationBinding annotBind : binds) {
					param.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
			} catch (AbortCompilation e) {
			}
			result.getParameters().add(param);
		}
		for (ITypeBinding typeBind : binding.getExceptionTypes()) {
			result.getExceptions().add(bindingToNamespaceClassifierReferenceConverter.convert(typeBind));
		}
		return result;
	}

}
