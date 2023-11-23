package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.operators.UnaryOperator;

public class UnaryExpressionPrinter {

	static void printUnaryExpression(UnaryExpression element, BufferedWriter writer) throws IOException {
		for (UnaryOperator op : element.getOperators()) {
			UnaryOperatorPrinter.printUnaryOperator(op, writer);
		}
		UnaryExpressionChildPrinter.printUnaryExpressionChild(element.getChild(), writer);
	}

}
