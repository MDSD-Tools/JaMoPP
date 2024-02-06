package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchCase;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class NormalSwitchCasePrinterImpl implements Printer<NormalSwitchCase> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public NormalSwitchCasePrinterImpl(final Printer<Expression> expressionPrinter,
			final Printer<Statement> statementPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final NormalSwitchCase element, final BufferedWriter writer) throws IOException {
		writer.append("case ");
		expressionPrinter.print(element.getCondition(), writer);
		for (final Expression expr : element.getAdditionalConditions()) {
			writer.append(", ");
			expressionPrinter.print(expr, writer);
		}
		writer.append(": ");
		for (final Statement s : element.getStatements()) {
			statementPrinter.print(s, writer);
		}
	}

}
