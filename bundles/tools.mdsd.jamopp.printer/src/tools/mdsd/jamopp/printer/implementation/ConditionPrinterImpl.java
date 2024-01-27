package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Condition;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionPrinterImpl implements Printer<Condition> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public ConditionPrinterImpl(final Printer<Expression> expressionPrinter,
			final Printer<Statement> statementPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final Condition element, final BufferedWriter writer) throws IOException {
		writer.append("if (");
		expressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		statementPrinter.print(element.getStatement(), writer);
		if (element.getElseStatement() != null) {
			writer.append("else\n");
			statementPrinter.print(element.getElseStatement(), writer);
		}
	}

}
