package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.NestedExpression;

class NestedExpressionPrinter {

	static void print(NestedExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(")");
	}

}
