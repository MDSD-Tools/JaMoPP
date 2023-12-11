package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.NormalSwitchCase;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class NormalSwitchCasePrinter implements Printer<NormalSwitchCase> {

	private final ExpressionPrinter ExpressionPrinter;
	private final StatementPrinter StatementPrinter;

	@Inject
	public NormalSwitchCasePrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter,
			jamopp.printer.implementation.StatementPrinter statementPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	public void print(NormalSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("case ");
		ExpressionPrinter.print(element.getCondition(), writer);
		for (Expression expr : element.getAdditionalConditions()) {
			writer.append(", ");
			ExpressionPrinter.print(expr, writer);
		}
		writer.append(": ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
	}

}
