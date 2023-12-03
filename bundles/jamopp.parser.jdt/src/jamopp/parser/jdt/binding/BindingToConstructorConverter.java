package jamopp.parser.jdt.binding;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.ToModifiersConverter;
import jamopp.parser.jdt.converter.ToTypeReferencesConverter;
import jamopp.parser.jdt.other.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilArrays;

public class BindingToConstructorConverter {

	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final ToTypeReferencesConverter toTypeReferencesConverter;
	private final UtilJdtResolver jdtTResolverUtility;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;
	private final BindingToNamespaceClassifierReferenceConverter bindingToNamespaceClassifierReferenceConverter;
	private final UtilArrays utilJdtBindingConverter;
	private final BindingToTypeParameterConverter bindingToTypeParameterConverter;
	private final ToModifiersConverter toModifiersConverter;

	@Inject
	BindingToConstructorConverter(UtilArrays utilJdtBindingConverter,
			ToTypeReferencesConverter toTypeReferencesConverter, ToModifiersConverter toModifiersConverter,
			ParametersFactory parametersFactory, LiteralsFactory literalsFactory, UtilJdtResolver jdtTResolverUtility,
			BindingToTypeParameterConverter bindingToTypeParameterConverter,
			BindingToTypeParameterConverter bindingToTypeParameterConverter2,
			BindingToNamespaceClassifierReferenceConverter bindingToNamespaceClassifierReferenceConverter,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
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

	Constructor convertToConstructor(IMethodBinding binding) {
		Constructor result = jdtTResolverUtility.getConstructor(binding);
		if (result.eContainer() != null) {
			return result;
		}
		result.getAnnotationsAndModifiers().addAll(toModifiersConverter.convert(binding.getModifiers()));
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers()
						.add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		try {
			for (ITypeBinding typeBind : binding.getTypeParameters()) {
				result.getTypeParameters().add(bindingToTypeParameterConverter.convertToTypeParameter(typeBind));
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
					param.getAnnotationsAndModifiers()
							.add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
				}
			} catch (AbortCompilation e) {
			}
			result.getParameters().add(param);
		}
		for (ITypeBinding typeBind : binding.getExceptionTypes()) {
			result.getExceptions().add(
					bindingToNamespaceClassifierReferenceConverter.convertToNamespaceClassifierReference(typeBind));
		}
		return result;
	}

}
