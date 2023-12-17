package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.PrefixExpression;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToUnaryExpressionConverter implements Converter<PrefixExpression, UnaryExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter;
	private final Converter<PrefixExpression.Operator, UnaryOperator> toUnaryOperatorConverter;

	@Inject
	ToUnaryExpressionConverter(ToUnaryOperatorConverter toUnaryOperatorConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
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
