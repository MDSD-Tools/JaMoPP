package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

public class HandlerAssignment extends Handler {

	private final ToAssignmentConverter toAssignmentOperatorConverter;
	private final UtilLayout utilLayout;
	private final ToExpressionConverter toExpressionConverter;
	private final ExpressionsFactory expressionsFactory;

	@Inject
	HandlerAssignment(UtilLayout utilLayout, ToExpressionConverter toExpressionConverter,
			ToAssignmentConverter toAssignmentOperatorConverter, ExpressionsFactory expressionsFactory) {
		this.toAssignmentOperatorConverter = toAssignmentOperatorConverter;
		this.utilLayout = utilLayout;
		this.toExpressionConverter = toExpressionConverter;
		this.expressionsFactory = expressionsFactory;
	}

	@Override
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		Assignment assign = (Assignment) expr;
		org.emftext.language.java.expressions.AssignmentExpression result = expressionsFactory
				.createAssignmentExpression();
		result.setChild((org.emftext.language.java.expressions.AssignmentExpressionChild) toExpressionConverter
				.convertToExpression(assign.getLeftHandSide()));
		result.setAssignmentOperator(toAssignmentOperatorConverter.convertToAssignmentOperator(assign.getOperator()));
		result.setValue(toExpressionConverter.convertToExpression(assign.getRightHandSide()));
		utilLayout.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
