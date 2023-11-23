package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;
import org.emftext.language.java.statements.Block;

public class LambdaExpressionPrinter {

	static void printLambdaExpression(LambdaExpression element, BufferedWriter writer) throws IOException {
		LambdaParametersPrinter.printLambdaParameters(element.getParameters(), writer);
		writer.append(" -> ");
		if (element.getBody() instanceof Block) {
			BlockPrinter.printBlock((Block) element.getBody(), writer);
		} else {
			ExpressionPrinter.printExpression((Expression) element.getBody(), writer);
		}
	}

}
