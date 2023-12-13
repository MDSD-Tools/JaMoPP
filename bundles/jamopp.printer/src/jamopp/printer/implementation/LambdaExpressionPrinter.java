package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;
import org.emftext.language.java.statements.Block;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.LambdaExpressionPrinterInt;

class LambdaExpressionPrinter implements Printer<LambdaExpression>, LambdaExpressionPrinterInt {

	private final LambdaParametersPrinter LambdaParametersPrinter;
	private final BlockPrinter BlockPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public LambdaExpressionPrinter(jamopp.printer.implementation.LambdaParametersPrinter lambdaParametersPrinter,
			jamopp.printer.implementation.BlockPrinter blockPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		LambdaParametersPrinter = lambdaParametersPrinter;
		BlockPrinter = blockPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(LambdaExpression element, BufferedWriter writer) throws IOException {
		LambdaParametersPrinter.print(element.getParameters(), writer);
		writer.append(" -> ");
		if (element.getBody() instanceof Block) {
			BlockPrinter.print((Block) element.getBody(), writer);
		} else {
			ExpressionPrinter.print((Expression) element.getBody(), writer);
		}
	}

}
