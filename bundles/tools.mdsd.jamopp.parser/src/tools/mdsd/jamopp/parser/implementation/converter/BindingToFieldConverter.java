package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;

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
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilArrays;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToFieldConverter implements Converter<IVariableBinding, Field> {

	private final JdtResolver jdtTResolverUtility;
	private final UtilArrays utilJdtBindingConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter;
	private final Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;

	@Inject
	public BindingToFieldConverter(final UtilArrays utilJdtBindingConverter,
			final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			final Converter<Integer, Collection<tools.mdsd.jamopp.model.java.modifiers.Modifier>> toModifiersConverter,
			final Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter,
			final JdtResolver jdtTResolverUtility,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.utilJdtBindingConverter = utilJdtBindingConverter;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.objectToPrimaryExpressionConverter = objectToPrimaryExpressionConverter;
		this.toModifiersConverter = toModifiersConverter;
	}

	@Override
	public Field convert(final IVariableBinding binding) {
		final ReferenceableElement refElement = jdtTResolverUtility.getReferencableElement(binding);
		Field result;
		if (refElement.eContainer() != null) {
			if (refElement instanceof AdditionalField) {
				result = (Field) ((AdditionalField) refElement).eContainer();
			} else {
				result = (Field) refElement;
			}
		} else {
			result = (Field) refElement;
			result.getAnnotationsAndModifiers().addAll(toModifiersConverter.convert(binding.getModifiers()));
			try {
				for (final IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotationsAndModifiers().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
			result.setName(binding.getName());
			result.setTypeReference(toTypeReferencesConverter.convert(binding.getType()).get(0));
			utilJdtBindingConverter.convertToArrayDimensionsAndSet(binding.getType(), result);
			if (binding.getConstantValue() != null) {
				result.setInitialValue(objectToPrimaryExpressionConverter.convert(binding.getConstantValue()));
			}
		}
		return result;
	}

}
