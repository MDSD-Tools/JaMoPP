package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;

public class AndExpressionPrinter {

	static void printAndExpression(AndExpression element, BufferedWriter writer) throws IOException {
		AndExpressionChildPrinter.printAndExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" & ");
			AndExpressionChildPrinter.printAndExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
