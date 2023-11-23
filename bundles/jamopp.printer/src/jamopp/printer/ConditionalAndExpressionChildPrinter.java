package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

class ConditionalAndExpressionChildPrinter {

	static void print(ConditionalAndExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof InclusiveOrExpression) {
			InclusiveOrExpressionPrinter.print((InclusiveOrExpression) element, writer);
		} else {
			InclusiveOrExpressionChildPrinter.print((InclusiveOrExpressionChild) element, writer);
		}
	}

}
