package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.ShiftExpressionChild;

public class ShiftExpressionChildPrinter {

	static void printShiftExpressionChild(ShiftExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof AdditiveExpression) {
			AdditiveExpressionPrinter.printAdditiveExpression((AdditiveExpression) element, writer);
		} else {
			AdditiveExpressionChildPrinter.printAdditiveExpressionChild((AdditiveExpressionChild) element, writer);
		}
	}

}
