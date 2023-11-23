package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;
import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;

public class UnaryExpressionChildPrinter {

	static void printUnaryExpressionChild(UnaryExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof PrefixUnaryModificationExpression) {
			PrefixUnaryModificationExpressionPrinter.printPrefixUnaryModificationExpression((PrefixUnaryModificationExpression) element, writer);
		} else if (element instanceof SuffixUnaryModificationExpression) {
			SuffixUnaryModificationExpressionPrinter.printSuffixUnaryModificationExpression((SuffixUnaryModificationExpression) element, writer);
		} else {
			UnaryModificationExpressionChildPrinter.printUnaryModificationExpressionChild((UnaryModificationExpressionChild) element, writer);
		}
	}

}
