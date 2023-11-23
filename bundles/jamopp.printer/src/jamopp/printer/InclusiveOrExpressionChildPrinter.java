package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

public class InclusiveOrExpressionChildPrinter {

	static void printInclusiveOrExpressionChild(InclusiveOrExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ExclusiveOrExpression) {
			ExclusiveOrExpressionPrinter.printExclusiveOrExpression((ExclusiveOrExpression) element, writer);
		} else {
			ExclusiveOrExpressionChildPrinter.printExclusiveOrExpressionChild((ExclusiveOrExpressionChild) element, writer);
		}
	}

}
