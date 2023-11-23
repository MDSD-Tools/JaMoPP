package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

class MultiplicativeExpressionChildPrinter {

	static void print(MultiplicativeExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof UnaryExpression) {
			UnaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			UnaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
