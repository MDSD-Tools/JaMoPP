package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.model.java.statements.WhileLoop;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class WhileLoopPrinterImpl implements Printer<WhileLoop> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public WhileLoopPrinterImpl(final Printer<Expression> expressionPrinter,
			final Printer<Statement> statementPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final WhileLoop element, final BufferedWriter writer) throws IOException {
		writer.append("while (");
		expressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		statementPrinter.print(element.getStatement(), writer);
	}

}
