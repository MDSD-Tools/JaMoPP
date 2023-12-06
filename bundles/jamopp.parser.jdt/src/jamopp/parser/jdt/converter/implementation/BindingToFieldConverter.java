package jamopp.parser.jdt.converter.implementation;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.expressions.PrimaryExpression;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.references.ReferenceableElement;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilArrays;
import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class BindingToFieldConverter implements ToConverter<IVariableBinding, Field> {

	private final UtilJdtResolver jdtTResolverUtility;
	private final UtilArrays utilJdtBindingConverter;
	private final ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final ToConverter<Object, PrimaryExpression> objectToPrimaryExpressionConverter;
	private final ToConverter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter;
	private final ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	BindingToFieldConverter(UtilArrays utilJdtBindingConverter,
			ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			ToConverter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter,
			ToConverter<Object, PrimaryExpression> objectToPrimaryExpressionConverter,
			UtilJdtResolver jdtTResolverUtility,
			ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.objectToPrimaryExpressionConverter = objectToPrimaryExpressionConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	@Override
	public Field convert(IVariableBinding binding) {
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
				result.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		result.setTypeReference(toTypeReferencesConverter.convert(binding.getType()).get(0));
		utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding.getType(), result);
		if (binding.getConstantValue() != null) {
			result.setInitialValue(objectToPrimaryExpressionConverter.convert(binding.getConstantValue()));
		}
		return result;
	}

}
