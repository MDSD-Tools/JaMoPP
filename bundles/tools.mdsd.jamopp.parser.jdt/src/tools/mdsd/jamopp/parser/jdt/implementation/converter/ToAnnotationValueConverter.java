package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.expressions.AssignmentExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToAnnotationValueConverter implements Converter<Expression, AnnotationValue> {

	private final Converter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierComverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter;

	@Inject
	ToAnnotationValueConverter(
			Converter<Expression, org.emftext.language.java.expressions.Expression> utilExpressionConverter,
			Converter<ArrayInitializer, org.emftext.language.java.arrays.ArrayInitializer> toArrayInitialisierComverter,
			Converter<Annotation, AnnotationInstance> toAnnotationInstanceConverter) {
		this.toArrayInitialisierComverter = toArrayInitialisierComverter;
		this.toExpressionConverter = utilExpressionConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	@Override
	public AnnotationValue convert(Expression expr) {
		if (expr instanceof Annotation) {
			return toAnnotationInstanceConverter.convert((Annotation) expr);
		}
		if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			return toArrayInitialisierComverter.convert((ArrayInitializer) expr);
		}
		return (AssignmentExpressionChild) toExpressionConverter.convert(expr);
	}

}
