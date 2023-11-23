package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.MultiplicativeExpressionChild;

class AdditiveExpressionChildPrinter {

	static void print(AdditiveExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof MultiplicativeExpression) {
			MultiplicativeExpressionPrinter.print((MultiplicativeExpression) element, writer);
		} else {
			MultiplicativeExpressionChildPrinter.print((MultiplicativeExpressionChild) element, writer);
		}
	}

}
