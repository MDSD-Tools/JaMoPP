package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;

class ConditionalAndExpressionPrinter {

	static void print(ConditionalAndExpression element, BufferedWriter writer)
			throws IOException {
		ConditionalAndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" && ");
			ConditionalAndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
