package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalOrExpression;

public class ConditionalOrExpressionPrinter {

	static void printConditionalOrExpression(ConditionalOrExpression element, BufferedWriter writer)
			throws IOException {
		ConditionalOrExpressionChildPrinter.printConditionalOrExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" || ");
			ConditionalOrExpressionChildPrinter.printConditionalOrExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
