package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpression;

public class ConditionalExpressionPrinter {

	static void printConditionalExpression(ConditionalExpression element, BufferedWriter writer)
			throws IOException {
		ConditionalExpressionChildPrinter.printConditionalExpressionChild(element.getChild(), writer);
		if (element.getExpressionIf() != null) {
			writer.append(" ? ");
			ExpressionPrinter.printExpression(element.getExpressionIf(), writer);
			writer.append(" : ");
			ExpressionPrinter.printExpression(element.getGeneralExpressionElse(), writer);
		}
	}

}
