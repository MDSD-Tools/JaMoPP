package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;

class ExpressionPrinter {

	static void print(Expression element, BufferedWriter writer) throws IOException {
		if (element instanceof LambdaExpression) {
			LambdaExpressionPrinter.print((LambdaExpression) element, writer);
		} else if (element instanceof AssignmentExpression) {
			AssignmentExpressionPrinter.print((AssignmentExpression) element, writer);
		} else {
			AssignmentExpressionChildPrinter.print((AssignmentExpressionChild) element, writer);
		}
	}

}
