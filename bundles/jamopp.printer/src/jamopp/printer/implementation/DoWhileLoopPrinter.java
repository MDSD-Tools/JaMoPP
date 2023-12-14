package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DoWhileLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.DoWhileLoopPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class DoWhileLoopPrinter implements DoWhileLoopPrinterInt {

	private final StatementPrinterInt StatementPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public DoWhileLoopPrinter(StatementPrinterInt statementPrinter, ExpressionPrinterInt expressionPrinter) {
		StatementPrinter = statementPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(DoWhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("do\n");
		StatementPrinter.print(element.getStatement(), writer);
		writer.append("while (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(");\n");
	}

}
