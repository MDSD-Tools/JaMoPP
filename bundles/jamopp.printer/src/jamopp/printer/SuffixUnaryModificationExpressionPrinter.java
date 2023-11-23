package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;

class SuffixUnaryModificationExpressionPrinter {

	static void print(SuffixUnaryModificationExpression element,
			BufferedWriter writer) throws IOException {
		UnaryModificationExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
	}

}
