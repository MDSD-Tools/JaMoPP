package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;

class AndExpressionChildPrinter {

	static void print(AndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			EqualityExpressionPrinter.print((EqualityExpression) element, writer);
		} else {
			EqualityExpressionChildPrinter.print((EqualityExpressionChild) element, writer);
		}
	}

}
