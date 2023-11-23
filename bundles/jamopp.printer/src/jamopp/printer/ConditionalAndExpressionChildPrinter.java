package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

public class ConditionalAndExpressionChildPrinter {

	static void printConditionalAndExpressionChild(ConditionalAndExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof InclusiveOrExpression) {
			InclusiveOrExpressionPrinter.printInclusiveOrExpression((InclusiveOrExpression) element, writer);
		} else {
			InclusiveOrExpressionChildPrinter.printInclusiveOrExpressionChild((InclusiveOrExpressionChild) element, writer);
		}
	}

}
