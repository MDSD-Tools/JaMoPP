package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;

class AssignmentExpressionPrinter {

	static void print(AssignmentExpression element, BufferedWriter writer)
			throws IOException {
		AssignmentExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getAssignmentOperator() != null) {
			AssignmentOperatorPrinter.print(element.getAssignmentOperator(), writer);
			ExpressionPrinter.print(element.getValue(), writer);
		}
	}

}
