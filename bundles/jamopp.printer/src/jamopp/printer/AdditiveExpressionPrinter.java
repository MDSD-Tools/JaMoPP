package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;

public class AdditiveExpressionPrinter {

	static void printAdditiveExpression(AdditiveExpression element, BufferedWriter writer) throws IOException {
		AdditiveExpressionChildPrinter.printAdditiveExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			AdditiveOperatorPrinter.printAdditiveOperator(element.getAdditiveOperators().get(index - 1), writer);
			AdditiveExpressionChildPrinter.printAdditiveExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
