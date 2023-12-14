package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.NormalSwitchCase;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class NormalSwitchCasePrinterImpl implements Printer<NormalSwitchCase> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public NormalSwitchCasePrinterImpl(Printer<Expression> expressionPrinter, Printer<Statement> statementPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(NormalSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("case ");
		this.expressionPrinter.print(element.getCondition(), writer);
		for (Expression expr : element.getAdditionalConditions()) {
			writer.append(", ");
			this.expressionPrinter.print(expr, writer);
		}
		writer.append(": ");
		for (Statement s : element.getStatements()) {
			this.statementPrinter.print(s, writer);
		}
	}

}
