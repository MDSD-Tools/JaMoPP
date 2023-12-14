package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.NormalSwitchCase;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.NormalSwitchCasePrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class NormalSwitchCasePrinter implements NormalSwitchCasePrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public NormalSwitchCasePrinter(ExpressionPrinterInt expressionPrinter, StatementPrinterInt statementPrinter) {
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
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
