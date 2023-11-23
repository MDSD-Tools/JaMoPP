package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpression;

public class EqualityExpressionPrinter {

	static void printEqualityExpression(EqualityExpression element, BufferedWriter writer) throws IOException {
		EqualityExpressionChildPrinter.printEqualityExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			EqualityOperatorPrinter.printEqualityOperator(element.getEqualityOperators().get(index - 1), writer);
			EqualityExpressionChildPrinter.printEqualityExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
