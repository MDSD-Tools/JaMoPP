package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.NestedExpression;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class NestedExpressionPrinterImpl implements Printer<NestedExpression> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public NestedExpressionPrinterImpl(Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(NestedExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		this.expressionPrinter.print(element.getExpression(), writer);
		writer.append(")");
	}

}
