package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.YieldStatement;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class YieldStatementPrinterImpl implements Printer<YieldStatement> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public YieldStatementPrinterImpl(Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		this.expressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
