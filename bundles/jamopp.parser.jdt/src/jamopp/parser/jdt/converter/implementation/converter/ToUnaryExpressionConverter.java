package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.PrefixExpression;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;
import org.emftext.language.java.operators.UnaryOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class ToUnaryExpressionConverter implements Converter<PrefixExpression, UnaryExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter;
	private final Converter<PrefixExpression.Operator, UnaryOperator> toUnaryOperatorConverter;

	@Inject
	ToUnaryExpressionConverter(ToUnaryOperatorConverter toUnaryOperatorConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter,
			IUtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.toUnaryOperatorConverter = toUnaryOperatorConverter;
	}

	@Override
	public UnaryExpression convert(PrefixExpression expr) {
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
