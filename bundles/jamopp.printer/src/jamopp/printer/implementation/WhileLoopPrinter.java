package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.WhileLoop;

import jamopp.printer.interfaces.Printer;

class WhileLoopPrinter implements Printer<WhileLoop>{

	private final ExpressionPrinter ExpressionPrinter;
	private final StatementPrinter StatementPrinter;
	
	public void print(WhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("while (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
