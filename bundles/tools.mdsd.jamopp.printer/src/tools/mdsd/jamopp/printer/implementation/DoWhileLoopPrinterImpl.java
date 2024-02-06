package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.DoWhileLoop;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class DoWhileLoopPrinterImpl implements Printer<DoWhileLoop> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public DoWhileLoopPrinterImpl(final Printer<Statement> statementPrinter,
			final Printer<Expression> expressionPrinter) {
		this.statementPrinter = statementPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final DoWhileLoop element, final BufferedWriter writer) throws IOException {
		writer.append("do\n");
		statementPrinter.print(element.getStatement(), writer);
		writer.append("while (");
		expressionPrinter.print(element.getCondition(), writer);
		writer.append(");\n");
	}

}
