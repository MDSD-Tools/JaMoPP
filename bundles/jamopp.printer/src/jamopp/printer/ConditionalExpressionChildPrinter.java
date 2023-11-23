package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpression;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

public class ConditionalExpressionChildPrinter {

	static void printConditionalExpressionChild(ConditionalExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ConditionalOrExpression) {
			ConditionalOrExpressionPrinter.printConditionalOrExpression((ConditionalOrExpression) element, writer);
		} else {
			ConditionalOrExpressionChildPrinter.printConditionalOrExpressionChild((ConditionalOrExpressionChild) element, writer);
		}
	}

}
