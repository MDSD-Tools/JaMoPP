package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

public class ConditionalOrExpressionChildPrinter {

	static void printConditionalOrExpressionChild(ConditionalOrExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ConditionalAndExpression) {
			ConditionalAndExpressionPrinter.printConditionalAndExpression((ConditionalAndExpression) element, writer);
		} else {
			ConditionalAndExpressionChildPrinter.printConditionalAndExpressionChild((ConditionalAndExpressionChild) element, writer);
		}
	}

}
