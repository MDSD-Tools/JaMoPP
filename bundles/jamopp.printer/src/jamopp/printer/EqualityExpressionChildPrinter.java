package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.InstanceOfExpression;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

class EqualityExpressionChildPrinter {

	static void print(EqualityExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof InstanceOfExpression) {
			InstanceOfExpressionPrinter.print((InstanceOfExpression) element, writer);
		} else {
			InstanceOfExpressionChildPrinter.print((InstanceOfExpressionChild) element, writer);
		}
	}

}
