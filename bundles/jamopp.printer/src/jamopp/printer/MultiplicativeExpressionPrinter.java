package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpression;

public class MultiplicativeExpressionPrinter {

	static void printMultiplicativeExpression(MultiplicativeExpression element, BufferedWriter writer)
			throws IOException {
		MultiplicativeExpressionChildPrinter.printMultiplicativeExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			MultiplicativeOperatorPrinter.printMultiplicativeOperator(element.getMultiplicativeOperators().get(index - 1), writer);
			MultiplicativeExpressionChildPrinter.printMultiplicativeExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
