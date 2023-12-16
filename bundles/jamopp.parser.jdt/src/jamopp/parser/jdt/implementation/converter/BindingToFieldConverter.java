package jamopp.parser.jdt.implementation.converter;

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

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilArrays;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;

@SuppressWarnings("restriction")
public class BindingToFieldConverter implements Converter<IVariableBinding, Field> {

	private final IUtilJdtResolver jdtTResolverUtility;
	private final IUtilArrays utilJdtBindingConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter;
	private final Converter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	BindingToFieldConverter(IUtilArrays utilJdtBindingConverter,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			Converter<Integer, Collection<org.emftext.language.java.modifiers.Modifier>> toModifiersConverter,
			Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter,
			IUtilJdtResolver jdtTResolverUtility,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
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
