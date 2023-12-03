package jamopp.parser.jdt.binding;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Method;
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

public class BindingToMethodConverter {

	private final StatementsFactory statementsFactory;
	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final ToTypeReferencesConverter toTypeReferencesConverter;
	private final UtilJdtResolver jdtTResolverUtility;
	private final UtilArrays utilJdtBindingConverter;
	private final BindingToTypeParameterConverter bindingToTypeParameterConverter;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;
	private final ObjectToAnnotationValueConverter objectToAnnotationValueConverter;
	private final BindingToNamespaceClassifierReferenceConverter bindingToNamespaceClassifierReferenceConverter;
	private final ToModifiersConverter toModifiersConverter;

	@Inject
	BindingToMethodConverter(UtilArrays utilJdtBindingConverter,
			ToTypeReferencesConverter toTypeReferencesConverter, ToModifiersConverter toModifiersConverter,
			StatementsFactory statementsFactory, ParametersFactory parametersFactory,
			ObjectToAnnotationValueConverter objectToAnnotationValueConverter, LiteralsFactory literalsFactory,
			UtilJdtResolver jdtTResolverUtility, BindingToTypeParameterConverter bindingToTypeParameterConverter,
			BindingToNamespaceClassifierReferenceConverter bindingToNamespaceClassifierReferenceConverter,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
		this.statementsFactory = statementsFactory;
		this.literalsFactory = literalsFactory;
		this.parametersFactory = parametersFactory;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.bindingToTypeParameterConverter = bindingToTypeParameterConverter;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.objectToAnnotationValueConverter = objectToAnnotationValueConverter;
		this.bindingToNamespaceClassifierReferenceConverter = bindingToNamespaceClassifierReferenceConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	Method convertToMethod(IMethodBinding binding) {
		Method result = jdtTResolverUtility.getMethod(binding);
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
		result.setTypeReference(toTypeReferencesConverter.convert(binding.getReturnType()).get(0));
		utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding.getReturnType(), result);
		try {
			for (ITypeBinding typeBind : binding.getTypeParameters()) {
				result.getTypeParameters().add(bindingToTypeParameterConverter.convertToTypeParameter(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.getDeclaredReceiverType() != null) {
			ReceiverParameter param = parametersFactory.createReceiverParameter();
			param.setTypeReference(toTypeReferencesConverter.convert(binding.getDeclaredReceiverType()).get(0));
			param.setName("");
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
			try {
				IAnnotationBinding[] binds = binding.getParameterAnnotations(index);
				for (IAnnotationBinding annotBind : binds) {
					param.getAnnotationsAndModifiers()
							.add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
				}
			} catch (AbortCompilation e) {
			}
			result.getParameters().add(param);
		}
		if (binding.getDefaultValue() != null) {
			((InterfaceMethod) result).setDefaultValue(
					objectToAnnotationValueConverter.convertToAnnotationValue(binding.getDefaultValue()));
		}
		try {
			for (ITypeBinding typeBind : binding.getExceptionTypes()) {
				result.getExceptions().add(
						bindingToNamespaceClassifierReferenceConverter.convertToNamespaceClassifierReference(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.getDeclaringClass().isInterface()) {
			boolean hasDefaultImpl = false;
			for (org.emftext.language.java.modifiers.Modifier mod : result.getModifiers()) {
				if (mod instanceof org.emftext.language.java.modifiers.Default) {
					hasDefaultImpl = true;
					break;
				}
			}
			if (!hasDefaultImpl) {
				result.setStatement(statementsFactory.createEmptyStatement());
			}
		}
		return result;
	}

}
