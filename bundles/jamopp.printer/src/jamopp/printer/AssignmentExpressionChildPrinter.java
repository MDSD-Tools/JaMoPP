package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;

class AssignmentExpressionChildPrinter {

	static void print(AssignmentExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ConditionalExpression) {
			ConditionalExpressionPrinter.print((ConditionalExpression) element, writer);
		} else {
			ConditionalExpressionChildPrinter.print((ConditionalExpressionChild) element, writer);
		}
	}

}
