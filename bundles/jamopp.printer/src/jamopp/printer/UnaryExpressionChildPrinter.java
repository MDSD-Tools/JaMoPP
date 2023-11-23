package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;
import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;

class UnaryExpressionChildPrinter {

	static void print(UnaryExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof PrefixUnaryModificationExpression) {
			PrefixUnaryModificationExpressionPrinter.print((PrefixUnaryModificationExpression) element, writer);
		} else if (element instanceof SuffixUnaryModificationExpression) {
			SuffixUnaryModificationExpressionPrinter.print((SuffixUnaryModificationExpression) element, writer);
		} else {
			UnaryModificationExpressionChildPrinter.print((UnaryModificationExpressionChild) element, writer);
		}
	}

}
