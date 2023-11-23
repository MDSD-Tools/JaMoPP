package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpression;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

class ConditionalExpressionChildPrinter {

	static void print(ConditionalExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ConditionalOrExpression) {
			ConditionalOrExpressionPrinter.print((ConditionalOrExpression) element, writer);
		} else {
			ConditionalOrExpressionChildPrinter.print((ConditionalOrExpressionChild) element, writer);
		}
	}

}
