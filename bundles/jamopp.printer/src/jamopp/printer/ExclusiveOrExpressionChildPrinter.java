package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;
import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;

public class ExclusiveOrExpressionChildPrinter {

	static void printExclusiveOrExpressionChild(ExclusiveOrExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof AndExpression) {
			AndExpressionPrinter.printAndExpression((AndExpression) element, writer);
		} else {
			AndExpressionChildPrinter.printAndExpressionChild((AndExpressionChild) element, writer);
		}
	}

}
