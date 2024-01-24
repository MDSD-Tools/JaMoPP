package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchRule;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class NormalSwitchRulePrinterImpl implements Printer<NormalSwitchRule> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public NormalSwitchRulePrinterImpl(final Printer<Expression> expressionPrinter,
			final Printer<Statement> statementPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final NormalSwitchRule element, final BufferedWriter writer) throws IOException {
		writer.append("case ");
		expressionPrinter.print(element.getCondition(), writer);
		for (final Expression expr : element.getAdditionalConditions()) {
			writer.append(", ");
			expressionPrinter.print(expr, writer);
		}
		writer.append(" -> ");
		for (final Statement s : element.getStatements()) {
			statementPrinter.print(s, writer);
		}
	}

}
