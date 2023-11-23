package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;

class AndExpressionPrinter {

	static void print(AndExpression element, BufferedWriter writer) throws IOException {
		AndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" & ");
			AndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
