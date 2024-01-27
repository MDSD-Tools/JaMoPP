package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.YieldStatement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class YieldStatementPrinterImpl implements Printer<YieldStatement> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public YieldStatementPrinterImpl(final Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final YieldStatement element, final BufferedWriter writer) throws IOException {
		writer.append("yield ");
		expressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
