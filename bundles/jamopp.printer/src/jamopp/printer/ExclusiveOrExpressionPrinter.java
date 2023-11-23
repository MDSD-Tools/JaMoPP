package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;

public class ExclusiveOrExpressionPrinter {

	static void printExclusiveOrExpression(ExclusiveOrExpression element, BufferedWriter writer)
			throws IOException {
		ExclusiveOrExpressionChildPrinter.printExclusiveOrExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" ^ ");
			ExclusiveOrExpressionChildPrinter.printExclusiveOrExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
