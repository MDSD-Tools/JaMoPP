package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.WhileLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.WhileLoopPrinterInt;

class WhileLoopPrinter implements WhileLoopPrinterInt {

	private final ExpressionPrinter ExpressionPrinter;
	private final StatementPrinter StatementPrinter;

	@Inject
	public WhileLoopPrinter(ExpressionPrinter expressionPrinter,
			StatementPrinter statementPrinter) {
		super();
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
