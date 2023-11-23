package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;

public class ExpressionPrinter {

	static void printExpression(Expression element, BufferedWriter writer) throws IOException {
		if (element instanceof LambdaExpression) {
			LambdaExpressionPrinter.printLambdaExpression((LambdaExpression) element, writer);
		} else if (element instanceof AssignmentExpression) {
			AssignmentExpressionPrinter.printAssignmentExpression((AssignmentExpression) element, writer);
		} else {
			AssignmentExpressionChildPrinter.printAssignmentExpressionChild((AssignmentExpressionChild) element, writer);
		}
	}

}
