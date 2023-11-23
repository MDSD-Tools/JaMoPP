package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.InstanceOfExpression;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

public class EqualityExpressionChildPrinter {

	static void printEqualityExpressionChild(EqualityExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof InstanceOfExpression) {
			InstanceOfExpressionPrinter.printInstanceOfExpression((InstanceOfExpression) element, writer);
		} else {
			InstanceOfExpressionChildPrinter.printInstanceOfExpressionChild((InstanceOfExpressionChild) element, writer);
		}
	}

}
