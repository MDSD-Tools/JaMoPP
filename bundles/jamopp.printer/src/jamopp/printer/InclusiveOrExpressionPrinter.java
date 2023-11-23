package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InclusiveOrExpression;

class InclusiveOrExpressionPrinter {

	static void print(InclusiveOrExpression element, BufferedWriter writer)
			throws IOException {
		InclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" | ");
			InclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
