package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;

class ExclusiveOrExpressionPrinter {

	static void print(ExclusiveOrExpression element, BufferedWriter writer)
			throws IOException {
		ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" ^ ");
			ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
