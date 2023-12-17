package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.model.java.statements.WhileLoop;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class WhileLoopPrinterImpl implements Printer<WhileLoop> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public WhileLoopPrinterImpl(Printer<Expression> expressionPrinter, Printer<Statement> statementPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(WhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("while (");
		this.expressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		this.statementPrinter.print(element.getStatement(), writer);
	}

}
