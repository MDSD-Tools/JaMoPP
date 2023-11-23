package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.MultiplicativeExpressionChild;

public class AdditiveExpressionChildPrinter {

	static void printAdditiveExpressionChild(AdditiveExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof MultiplicativeExpression) {
			MultiplicativeExpressionPrinter.printMultiplicativeExpression((MultiplicativeExpression) element, writer);
		} else {
			MultiplicativeExpressionChildPrinter.printMultiplicativeExpressionChild((MultiplicativeExpressionChild) element, writer);
		}
	}

}