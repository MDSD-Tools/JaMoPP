package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Condition;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class ConditionPrinterImpl implements Printer<Condition> {

	private final Printer<Expression> ExpressionPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public ConditionPrinterImpl(Printer<Expression> expressionPrinter, StatementPrinterInt statementPrinter) {
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
