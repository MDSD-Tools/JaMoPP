package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;

public class AssignmentExpressionPrinter {

	static void printAssignmentExpression(AssignmentExpression element, BufferedWriter writer)
			throws IOException {
		AssignmentExpressionChildPrinter.printAssignmentExpressionChild(element.getChild(), writer);
		if (element.getAssignmentOperator() != null) {
			AssignmentOperatorPrinter.printAssignmentOperator(element.getAssignmentOperator(), writer);
			ExpressionPrinter.printExpression(element.getValue(), writer);
		}
	}

}
