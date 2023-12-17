package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

@SuppressWarnings("restriction")
public class BindingToFieldConverter implements Converter<IVariableBinding, Field> {

	private final UtilJdtResolver jdtTResolverUtility;
	private final UtilArrays utilJdtBindingConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter;
	private final Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	BindingToFieldConverter(UtilArrays utilJdtBindingConverter,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter,
			Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter,
			UtilJdtResolver jdtTResolverUtility,
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
