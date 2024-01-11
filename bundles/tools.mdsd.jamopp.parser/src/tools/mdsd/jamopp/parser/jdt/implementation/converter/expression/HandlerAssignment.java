package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.operators.AssignmentOperator;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class HandlerAssignment implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final Converter<Assignment.Operator, AssignmentOperator> toAssignmentOperatorConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;

	@Inject
	public HandlerAssignment(UtilLayout utilLayout,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			Converter<Assignment.Operator, AssignmentOperator> toAssignmentOperatorConverter,
			ExpressionsFactory expressionsFactory) {
		this.toAssignmentOperatorConverter = toAssignmentOperatorConverter;
		this.utilLayout = utilLayout;
		this.toExpressionConverter = toExpressionConverter;
		this.expressionsFactory = expressionsFactory;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		Assignment assign = (Assignment) expr;
		tools.mdsd.jamopp.model.java.expressions.AssignmentExpression result = expressionsFactory
				.createAssignmentExpression();
		result.setChild((tools.mdsd.jamopp.model.java.expressions.AssignmentExpressionChild) toExpressionConverter
				.convert(assign.getLeftHandSide()));
		result.setAssignmentOperator(toAssignmentOperatorConverter.convert(assign.getOperator()));
		result.setValue(toExpressionConverter.convert(assign.getRightHandSide()));
		utilLayout.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
