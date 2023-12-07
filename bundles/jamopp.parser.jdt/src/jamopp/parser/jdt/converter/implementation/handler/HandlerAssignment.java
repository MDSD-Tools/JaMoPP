package jamopp.parser.jdt.converter.implementation.handler;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.AssignmentOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class HandlerAssignment implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final IUtilLayout utilLayout;
	private final Converter<Assignment.Operator, AssignmentOperator> toAssignmentOperatorConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;

	@Inject
	HandlerAssignment(IUtilLayout utilLayout,
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			Converter<Assignment.Operator, AssignmentOperator> toAssignmentOperatorConverter,
			ExpressionsFactory expressionsFactory) {
		this.toAssignmentOperatorConverter = toAssignmentOperatorConverter;
		this.utilLayout = utilLayout;
		this.toExpressionConverter = toExpressionConverter;
		this.expressionsFactory = expressionsFactory;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		Assignment assign = (Assignment) expr;
		org.emftext.language.java.expressions.AssignmentExpression result = expressionsFactory
				.createAssignmentExpression();
		result.setChild((org.emftext.language.java.expressions.AssignmentExpressionChild) toExpressionConverter
				.convert(assign.getLeftHandSide()));
		result.setAssignmentOperator(toAssignmentOperatorConverter.convert(assign.getOperator()));
		result.setValue(toExpressionConverter.convert(assign.getRightHandSide()));
		utilLayout.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
