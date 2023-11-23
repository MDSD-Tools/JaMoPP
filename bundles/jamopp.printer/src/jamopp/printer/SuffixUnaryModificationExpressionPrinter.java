package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;

public class SuffixUnaryModificationExpressionPrinter {

	static void printSuffixUnaryModificationExpression(SuffixUnaryModificationExpression element,
			BufferedWriter writer) throws IOException {
		UnaryModificationExpressionChildPrinter.printUnaryModificationExpressionChild(element.getChild(), writer);
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.printUnaryModificationOperator(element.getOperator(), writer);
		}
	}

}
