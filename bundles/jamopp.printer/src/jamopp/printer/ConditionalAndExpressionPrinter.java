package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;

public class ConditionalAndExpressionPrinter {

	static void printConditionalAndExpression(ConditionalAndExpression element, BufferedWriter writer)
			throws IOException {
		ConditionalAndExpressionChildPrinter.printConditionalAndExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" && ");
			ConditionalAndExpressionChildPrinter.printConditionalAndExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
