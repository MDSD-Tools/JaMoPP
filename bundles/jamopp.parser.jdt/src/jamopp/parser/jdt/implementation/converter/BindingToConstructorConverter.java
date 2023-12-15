package jamopp.parser.jdt.implementation.converter;

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
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilArrays;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

@SuppressWarnings("restriction")
public class BindingToConstructorConverter implements Converter<IMethodBinding, Constructor> {

	private final UtilJdtResolver jdtTResolverUtility;
	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final Converter<IAnnotationBinding, AnnotationInstance> toAnnotationInstanceConverter;
	private final Converter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter;
	private final Converter<ITypeBinding, NamespaceClassifierReference> toNamespaceClassifierReferenceConverter;
	private final Converter<ITypeBinding, TypeParameter> toTypeParameterConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final UtilArrays utilJdtBindingConverter;

	@Inject
	BindingToConstructorConverter(UtilArrays utilJdtBindingConverter,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			Converter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter,
			ParametersFactory parametersFactory, LiteralsFactory literalsFactory, UtilJdtResolver jdtTResolverUtility,
			Converter<ITypeBinding, TypeParameter> bindingToTypeParameterConverter,
			Converter<ITypeBinding, NamespaceClassifierReference> bindingToNamespaceClassifierReferenceConverter,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.literalsFactory = literalsFactory;
		this.parametersFactory = parametersFactory;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.toAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.toNamespaceClassifierReferenceConverter = bindingToNamespaceClassifierReferenceConverter;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.toTypeParameterConverter = bindingToTypeParameterConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	@Override
	public Constructor convert(IMethodBinding binding) {
		var result = this.jdtTResolverUtility.getConstructor(binding);
		if (result.eContainer() != null) {
			return result;
		}
		result.getAnnotationsAndModifiers().addAll(this.toModifiersConverter.convert(binding.getModifiers()));
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers().add(this.toAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		try {
			for (ITypeBinding typeBind : binding.getTypeParameters()) {
				result.getTypeParameters().add(this.toTypeParameterConverter.convert(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.getDeclaredReceiverType() != null) {
			var param = this.parametersFactory.createReceiverParameter();
			param.setName("");
			param.setTypeReference(this.toTypeReferencesConverter.convert(binding.getDeclaredReceiverType()).get(0));
			param.setOuterTypeReference(param.getTypeReference());
			param.setThisReference(this.literalsFactory.createThis());
			result.getParameters().add(param);
		}
		for (var index = 0; index < binding.getParameterTypes().length; index++) {
			var typeBind = binding.getParameterTypes()[index];
			Parameter param;
			if (binding.isVarargs() && index == binding.getParameterTypes().length - 1) {
				param = this.parametersFactory.createVariableLengthParameter();
			} else {
				param = this.parametersFactory.createOrdinaryParameter();
			}
			param.setName("param" + index);
			param.setTypeReference(this.toTypeReferencesConverter.convert(typeBind).get(0));
			this.utilJdtBindingConverter.convertToArrayDimensionsAndSet(typeBind, param);
			var binds = binding.getParameterAnnotations(index);
			try {
				for (IAnnotationBinding annotBind : binds) {
					param.getAnnotationsAndModifiers().add(this.toAnnotationInstanceConverter.convert(annotBind));
				}
			} catch (AbortCompilation e) {
			}
			result.getParameters().add(param);
		}
		for (ITypeBinding typeBind : binding.getExceptionTypes()) {
			result.getExceptions().add(this.toNamespaceClassifierReferenceConverter.convert(typeBind));
		}
		return result;
	}

}
