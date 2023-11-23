package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;

public class AndExpressionChildPrinter {

	static void printAndExpressionChild(AndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			EqualityExpressionPrinter.printEqualityExpression((EqualityExpression) element, writer);
		} else {
			EqualityExpressionChildPrinter.printEqualityExpressionChild((EqualityExpressionChild) element, writer);
		}
	}

}
