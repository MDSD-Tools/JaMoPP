package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

import com.google.inject.Inject;

class HandlerInstanceOf extends Handler {

	private final UtilLayout utilLayout;
	private final ToExpressionConverter toExpressionConverter;
	private final ExpressionsFactory expressionsFactory;
	private final ToTypeReferenceConverter toTypeReferenceConverter;

	@Inject
	HandlerInstanceOf(UtilLayout utilLayout, ToTypeReferenceConverter toTypeReferenceConverter,
			ToExpressionConverter toExpressionConverter, ExpressionsFactory expressionsFactory) {
		this.utilLayout = utilLayout;
		this.toExpressionConverter = toExpressionConverter;
		this.expressionsFactory = expressionsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
	}

	@Override
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		InstanceofExpression castedExpr = (InstanceofExpression) expr;
		org.emftext.language.java.expressions.InstanceOfExpression result = expressionsFactory
				.createInstanceOfExpression();
		result.setChild(
				(InstanceOfExpressionChild) toExpressionConverter.convertToExpression(castedExpr.getLeftOperand()));
		result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(castedExpr.getRightOperand()));
		toTypeReferenceConverter.convertToArrayDimensionsAndSet(castedExpr.getRightOperand(), result);
		utilLayout.convertToMinimalLayoutInformation(result, castedExpr);
		return result;
	}

}
