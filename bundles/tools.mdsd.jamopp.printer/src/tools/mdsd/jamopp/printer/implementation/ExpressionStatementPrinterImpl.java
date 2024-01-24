package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.ExpressionStatement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExpressionStatementPrinterImpl implements Printer<ExpressionStatement> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public ExpressionStatementPrinterImpl(final Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final ExpressionStatement element, final BufferedWriter writer) throws IOException {
		expressionPrinter.print(element.getExpression(), writer);
		writer.append(";\n");
	}

}
