package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.LambdaExpression;
import tools.mdsd.jamopp.model.java.expressions.LambdaParameters;
import tools.mdsd.jamopp.model.java.statements.Block;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class LambdaExpressionPrinterImpl implements Printer<LambdaExpression> {

	private final Printer<Block> blockPrinter;
	private final Printer<Expression> expressionPrinter;
	private final Printer<LambdaParameters> lambdaParametersPrinter;

	@Inject
	public LambdaExpressionPrinterImpl(Printer<LambdaParameters> lambdaParametersPrinter, Printer<Block> blockPrinter,
			Printer<Expression> expressionPrinter) {
		this.lambdaParametersPrinter = lambdaParametersPrinter;
		this.blockPrinter = blockPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(LambdaExpression element, BufferedWriter writer) throws IOException {
		this.lambdaParametersPrinter.print(element.getParameters(), writer);
		writer.append(" -> ");
		if (element.getBody() instanceof Block) {
			this.blockPrinter.print((Block) element.getBody(), writer);
		} else {
			this.expressionPrinter.print((Expression) element.getBody(), writer);
		}
	}

}
