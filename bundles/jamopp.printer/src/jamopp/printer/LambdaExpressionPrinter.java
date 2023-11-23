package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;
import org.emftext.language.java.statements.Block;

class LambdaExpressionPrinter {

	static void print(LambdaExpression element, BufferedWriter writer) throws IOException {
		LambdaParametersPrinter.print(element.getParameters(), writer);
		writer.append(" -> ");
		if (element.getBody() instanceof Block) {
			BlockPrinter.print((Block) element.getBody(), writer);
		} else {
			ExpressionPrinter.print((Expression) element.getBody(), writer);
		}
	}

}
