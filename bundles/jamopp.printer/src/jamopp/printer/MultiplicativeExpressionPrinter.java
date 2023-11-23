package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpression;

class MultiplicativeExpressionPrinter {

	static void print(MultiplicativeExpression element, BufferedWriter writer)
			throws IOException {
		MultiplicativeExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			MultiplicativeOperatorPrinter.print(element.getMultiplicativeOperators().get(index - 1), writer);
			MultiplicativeExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
