package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.expressions.AssignmentExpressionChild;

class ToAnnotationValueConverter {
	
	private final UtilBaseConverter utilBaseConverter;
	private final ToArrayInitialisierConverter toArrayInitialisierComverter;
	private final UtilExpressionConverter utilExpressionConverter;
	
	public ToAnnotationValueConverter(UtilExpressionConverter utilExpressionConverter, UtilBaseConverter utilBaseConverter, ToArrayInitialisierConverter toArrayInitialisierComverter) {
		this.utilBaseConverter = utilBaseConverter;
		this.toArrayInitialisierComverter = toArrayInitialisierComverter;
		this.utilExpressionConverter = utilExpressionConverter;
	}

	AnnotationValue convertToAnnotationValue(Expression expr) {
		if (expr instanceof Annotation) {
			return utilBaseConverter.convertToAnnotationInstance((Annotation) expr);
		}
		if (expr.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
			return toArrayInitialisierComverter.convertToArrayInitializer((ArrayInitializer) expr);
		}
		return (AssignmentExpressionChild) utilExpressionConverter.convertToExpression(expr);
	}
	
}
