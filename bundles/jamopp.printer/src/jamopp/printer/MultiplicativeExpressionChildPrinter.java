package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

public class MultiplicativeExpressionChildPrinter {

	static void printMultiplicativeExpressionChild(MultiplicativeExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof UnaryExpression) {
			UnaryExpressionPrinter.printUnaryExpression((UnaryExpression) element, writer);
		} else {
			UnaryExpressionChildPrinter.printUnaryExpressionChild((UnaryExpressionChild) element, writer);
		}
	}

}
