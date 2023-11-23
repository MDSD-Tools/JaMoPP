package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;

class PrefixUnaryModificationExpressionPrinter {

	static void print(PrefixUnaryModificationExpression element,
			BufferedWriter writer) throws IOException {
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
		UnaryModificationExpressionChildPrinter.print(element.getChild(), writer);
	}

}
