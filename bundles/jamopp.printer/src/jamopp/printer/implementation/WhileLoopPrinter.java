package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.WhileLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;
import jamopp.printer.interfaces.printer.WhileLoopPrinterInt;

public class WhileLoopPrinter implements WhileLoopPrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public WhileLoopPrinter(ExpressionPrinterInt expressionPrinter, StatementPrinterInt statementPrinter) {
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(WhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("while (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
