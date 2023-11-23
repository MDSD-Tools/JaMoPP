package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;

public class AssignmentExpressionChildPrinter {

	static void printAssignmentExpressionChild(AssignmentExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ConditionalExpression) {
			ConditionalExpressionPrinter.printConditionalExpression((ConditionalExpression) element, writer);
		} else {
			ConditionalExpressionChildPrinter.printConditionalExpressionChild((ConditionalExpressionChild) element, writer);
		}
	}

}
