package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpressionChild;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToAnnotationValueConverter implements Converter<Expression, AnnotationValue> {

	private final Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer> toArrayInitialisierComverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	public ToAnnotationValueConverter(
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> utilExpressionConverter,
			final Converter<ArrayInitializer, tools.mdsd.jamopp.model.java.arrays.ArrayInitializer> toArrayInitialisierComverter,
			final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.toArrayInitialisierComverter = toArrayInitialisierComverter;
		toExpressionConverter = utilExpressionConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@Override
	public AnnotationValue convert(final Expression expr) {
		AnnotationValue result;
		if (expr instanceof Annotation) {
			result = toAnnotationInstanceConverter.convert((Annotation) expr);
		} else if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			result = toArrayInitialisierComverter.convert((ArrayInitializer) expr);
		} else {
			result = (AssignmentExpressionChild) toExpressionConverter.convert(expr);
		}
		return result;
	}

}
