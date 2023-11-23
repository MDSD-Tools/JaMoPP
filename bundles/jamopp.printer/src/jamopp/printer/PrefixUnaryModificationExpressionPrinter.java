package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;

public class PrefixUnaryModificationExpressionPrinter {

	static void printPrefixUnaryModificationExpression(PrefixUnaryModificationExpression element,
			BufferedWriter writer) throws IOException {
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.printUnaryModificationOperator(element.getOperator(), writer);
		}
		UnaryModificationExpressionChildPrinter.printUnaryModificationExpressionChild(element.getChild(), writer);
	}

}
