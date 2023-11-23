package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

class RelationExpressionChildPrinter {

	static void print(RelationExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ShiftExpression) {
			ShiftExpressionPrinter.print((ShiftExpression) element, writer);
		} else {
			ShiftExpressionChildPrinter.print((ShiftExpressionChild) element, writer);
		}
	}

}
