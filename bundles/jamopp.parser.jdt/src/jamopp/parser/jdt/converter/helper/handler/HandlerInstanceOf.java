package jamopp.parser.jdt.converter.helper.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.implementation.ToTypeReferenceConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class HandlerInstanceOf implements ExpressionHandler {

	private final UtilLayout utilLayout;
	private final ToExpressionConverter toExpressionConverter;
	private final ExpressionsFactory expressionsFactory;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;

	@Inject
	HandlerInstanceOf(UtilLayout utilLayout, ToTypeReferenceConverter toTypeReferenceConverter,
			ToExpressionConverter toExpressionConverter, ExpressionsFactory expressionsFactory,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.utilLayout = utilLayout;
		this.toExpressionConverter = toExpressionConverter;
		this.expressionsFactory = expressionsFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		InstanceofExpression castedExpr = (InstanceofExpression) expr;
		org.emftext.language.java.expressions.InstanceOfExpression result = expressionsFactory
				.createInstanceOfExpression();
		result.setChild((InstanceOfExpressionChild) toExpressionConverter.convert(castedExpr.getLeftOperand()));
		result.setTypeReference(toTypeReferenceConverter.convert(castedExpr.getRightOperand()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(castedExpr.getRightOperand(), result);
		utilLayout.convertToMinimalLayoutInformation(result, castedExpr);
		return result;
	}

}
