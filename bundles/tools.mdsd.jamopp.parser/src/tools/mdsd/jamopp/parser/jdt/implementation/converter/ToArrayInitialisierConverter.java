package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.arrays.ArraysFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToArrayInitialisierConverter
		implements Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer> {

	private final ArraysFactory arraysFactory;
	private final UtilLayout utilLayout;
	private final Provider<Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression>> utilExpressionConverter;
	private final Provider<Converter<Annotation, AnnotationInstance>> toAnnotationInstanceConverter;

	@Inject
	public ToArrayInitialisierConverter(final UtilLayout utilLayout, final ArraysFactory arraysFactory,
			final Provider<Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression>> utilExpressionConverter,
			final Provider<Converter<Annotation, AnnotationInstance>> toAnnotationInstanceConverter) {
		this.arraysFactory = arraysFactory;
		this.utilLayout = utilLayout;
		this.utilExpressionConverter = utilExpressionConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public tools.mdsd.jamopp.model.java.arrays.ArrayInitializer convert(final ArrayInitializer arr) {
		final tools.mdsd.jamopp.model.java.arrays.ArrayInitializer result = arraysFactory.createArrayInitializer();
		arr.expressions().forEach(obj -> {
			tools.mdsd.jamopp.model.java.arrays.ArrayInitializationValue value;
			final Expression expr = (Expression) obj;
			if (expr instanceof ArrayInitializer) {
				value = convert((ArrayInitializer) expr);
			} else if (expr instanceof Annotation) {
				value = toAnnotationInstanceConverter.get().convert((Annotation) expr);
			} else {
				value = utilExpressionConverter.get().convert(expr);
			}
			result.getInitialValues().add(value);
		});
		utilLayout.convertToMinimalLayoutInformation(result, arr);
		return result;
	}

}
