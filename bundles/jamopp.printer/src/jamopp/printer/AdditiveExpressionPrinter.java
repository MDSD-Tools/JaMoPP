package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;

class AdditiveExpressionPrinter {

	static void print(AdditiveExpression element, BufferedWriter writer) throws IOException {
		AdditiveExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			AdditiveOperatorPrinter.print(element.getAdditiveOperators().get(index - 1), writer);
			AdditiveExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
