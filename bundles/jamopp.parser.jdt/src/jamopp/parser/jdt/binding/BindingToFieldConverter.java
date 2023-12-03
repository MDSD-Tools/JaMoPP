package jamopp.parser.jdt.binding;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.references.ReferenceableElement;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToModifiersConverter;
import jamopp.parser.jdt.converter.ToTypeReferencesConverter;
import jamopp.parser.jdt.other.UtilArrays;
import jamopp.parser.jdt.other.UtilJdtResolver;

public class BindingToFieldConverter {

	private final ToTypeReferencesConverter toTypeReferencesConverter;
	private final UtilJdtResolver jdtTResolverUtility;
	private final UtilArrays utilJdtBindingConverter;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;
	private final ObjectToPrimaryExpressionConverter objectToPrimaryExpressionConverter;
	private final ToModifiersConverter toModifiersConverter;

	@Inject
	BindingToFieldConverter(UtilArrays utilJdtBindingConverter,
			UtilArrays utilJdtBindingConverter2, ToTypeReferencesConverter toTypeReferencesConverter,
			ToModifiersConverter toModifiersConverter, ToModifiersConverter toModifiersConverter2,
			ObjectToPrimaryExpressionConverter objectToPrimaryExpressionConverter, UtilJdtResolver jdtTResolverUtility,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.objectToPrimaryExpressionConverter = objectToPrimaryExpressionConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	Field convertToField(IVariableBinding binding) {
		ReferenceableElement refElement = jdtTResolverUtility.getReferencableElement(binding);
		if (refElement.eContainer() != null) {
			if (refElement instanceof AdditionalField) {
				return (Field) ((AdditionalField) refElement).eContainer();
			}
			return (Field) refElement;
		}
		Field result = (Field) refElement;
		result.getAnnotationsAndModifiers().addAll(toModifiersConverter.convert(binding.getModifiers()));
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers()
						.add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		result.setTypeReference(toTypeReferencesConverter.convert(binding.getType()).get(0));
		utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding.getType(), result);
		if (binding.getConstantValue() != null) {
			result.setInitialValue(
					objectToPrimaryExpressionConverter.convertToPrimaryExpression(binding.getConstantValue()));
		}
		return result;
	}

}
