package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Condition;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConditionPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class ConditionPrinter implements ConditionPrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public ConditionPrinter(ExpressionPrinterInt expressionPrinter, StatementPrinterInt statementPrinter) {
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(Condition element, BufferedWriter writer) throws IOException {
		writer.append("if (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
		if (element.getElseStatement() != null) {
			writer.append("else\n");
			StatementPrinter.print(element.getElseStatement(), writer);
		}
	}

}
