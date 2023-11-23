package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalOrExpression;

class ConditionalOrExpressionPrinter {

	static void print(ConditionalOrExpression element, BufferedWriter writer)
			throws IOException {
		ConditionalOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" || ");
			ConditionalOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
