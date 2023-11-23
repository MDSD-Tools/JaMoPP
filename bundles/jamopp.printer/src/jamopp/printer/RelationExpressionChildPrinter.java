package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

public class RelationExpressionChildPrinter {

	static void printRelationExpressionChild(RelationExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ShiftExpression) {
			ShiftExpressionPrinter.printShiftExpression((ShiftExpression) element, writer);
		} else {
			ShiftExpressionChildPrinter.printShiftExpressionChild((ShiftExpressionChild) element, writer);
		}
	}

}
