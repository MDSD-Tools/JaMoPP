package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;

class AndExpressionChildPrinter {

	static void print(AndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			EqualityExpressionPrinter.printEqualityExpression((EqualityExpression) element, writer);
		} else {
			EqualityExpressionChildPrinter.printEqualityExpressionChild((EqualityExpressionChild) element, writer);
		}
	}

}
