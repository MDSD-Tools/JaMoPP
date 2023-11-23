package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.NestedExpression;

public class NestedExpressionPrinter {

	static void printNestedExpression(NestedExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		ExpressionPrinter.printExpression(element.getExpression(), writer);
		writer.append(")");
	}

}
