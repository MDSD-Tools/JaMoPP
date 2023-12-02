package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.PrefixExpression;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

import com.google.inject.Inject;

class ToUnaryExpressionConverter extends ToConverter<PrefixExpression, UnaryExpression> {

	private final UtilLayout layoutInformationConverter;
	private final ExpressionsFactory expressionsFactory;
	private final ToExpressionConverter toExpressionConverter;
	private final ToUnaryOperatorConverter toUnaryOperatorConverter;

	@Inject
	ToUnaryExpressionConverter(ToUnaryOperatorConverter toUnaryOperatorConverter,
			ToExpressionConverter toExpressionConverter, UtilLayout layoutInformationConverter,
			ExpressionsFactory expressionsFactory) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.toUnaryOperatorConverter = toUnaryOperatorConverter;
	}

	@Override
	UnaryExpression convert(PrefixExpression expr) {
		UnaryExpression result = expressionsFactory.createUnaryExpression();
		result.getOperators().add(toUnaryOperatorConverter.convert(expr.getOperator()));
		Expression potChild = toExpressionConverter.convert(expr.getOperand());
		if (potChild instanceof UnaryExpressionChild) {
			result.setChild((UnaryExpressionChild) potChild);
		} else {
			UnaryExpression secRes = (UnaryExpression) potChild;
			result.getOperators().addAll(secRes.getOperators());
			result.setChild(secRes.getChild());
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
