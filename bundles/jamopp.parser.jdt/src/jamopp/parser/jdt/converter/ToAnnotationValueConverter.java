package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.expressions.AssignmentExpressionChild;

import com.google.inject.Inject;

import jamopp.parser.jdt.other.UtilExpressionConverter;

public class ToAnnotationValueConverter {

	private final ToArrayInitialisierConverter toArrayInitialisierComverter;
	private final UtilExpressionConverter utilExpressionConverter;
	private final ToAnnotationInstanceConverter toAnnotationInstanceConverter;

	@Inject
	ToAnnotationValueConverter(UtilExpressionConverter utilExpressionConverter,
			ToArrayInitialisierConverter toArrayInitialisierComverter,
			ToAnnotationInstanceConverter toAnnotationInstanceConverter) {
		this.toArrayInitialisierComverter = toArrayInitialisierComverter;
		this.utilExpressionConverter = utilExpressionConverter;
		this.toAnnotationInstanceConverter = toAnnotationInstanceConverter;
	}

	public AnnotationValue convertToAnnotationValue(Expression expr) {
		if (expr instanceof Annotation) {
			return toAnnotationInstanceConverter.convertToAnnotationInstance((Annotation) expr);
		}
		if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			return toArrayInitialisierComverter.convertToArrayInitializer((ArrayInitializer) expr);
		}
		return (AssignmentExpressionChild) utilExpressionConverter.convertToExpression(expr);
	}

}