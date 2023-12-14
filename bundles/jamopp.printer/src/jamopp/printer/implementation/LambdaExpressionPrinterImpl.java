package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;
import org.emftext.language.java.expressions.LambdaParameters;
import org.emftext.language.java.statements.Block;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class LambdaExpressionPrinterImpl implements Printer<LambdaExpression> {

	private final Printer<Block> BlockPrinter;
	private final Printer<Expression> ExpressionPrinter;
	private final Printer<LambdaParameters> LambdaParametersPrinter;

	@Inject
	public LambdaExpressionPrinterImpl(Printer<LambdaParameters> lambdaParametersPrinter, Printer<Block> blockPrinter,
			Printer<Expression> expressionPrinter) {
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
