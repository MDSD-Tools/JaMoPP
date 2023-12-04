package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.expressions.AssignmentExpressionChild;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;

public class ToAnnotationValueConverter implements ToConverter<Expression, AnnotationValue> {

	private final ToArrayInitialisierConverter toArrayInitialisierComverter;
	private final ToExpressionConverter utilExpressionConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;

	@Inject
	ToAnnotationValueConverter(ToExpressionConverter utilExpressionConverter,
			ToArrayInitialisierConverter toArrayInitialisierComverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.toArrayInitialisierComverter = toArrayInitialisierComverter;
		this.utilExpressionConverter = utilExpressionConverter;
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
		return (AssignmentExpressionChild) utilExpressionConverter.convert(expr);
	}

}
