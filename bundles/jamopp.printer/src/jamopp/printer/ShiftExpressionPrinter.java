package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ShiftExpression;

public class ShiftExpressionPrinter {

	static void printShiftExpression(ShiftExpression element, BufferedWriter writer) throws IOException {
		ShiftExpressionChildPrinter.printShiftExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			ShiftOperatorPrinter.printShiftOperator(element.getShiftOperators().get(index - 1), writer);
			ShiftExpressionChildPrinter.printShiftExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
