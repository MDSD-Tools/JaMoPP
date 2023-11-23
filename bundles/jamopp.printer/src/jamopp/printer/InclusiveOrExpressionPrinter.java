package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InclusiveOrExpression;

public class InclusiveOrExpressionPrinter {

	static void printInclusiveOrExpression(InclusiveOrExpression element, BufferedWriter writer)
			throws IOException {
		InclusiveOrExpressionChildPrinter.printInclusiveOrExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" | ");
			InclusiveOrExpressionChildPrinter.printInclusiveOrExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
