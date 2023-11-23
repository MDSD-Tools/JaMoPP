package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

class InclusiveOrExpressionChildPrinter {

	static void print(InclusiveOrExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ExclusiveOrExpression) {
			ExclusiveOrExpressionPrinter.print((ExclusiveOrExpression) element, writer);
		} else {
			ExclusiveOrExpressionChildPrinter.print((ExclusiveOrExpressionChild) element, writer);
		}
	}

}
