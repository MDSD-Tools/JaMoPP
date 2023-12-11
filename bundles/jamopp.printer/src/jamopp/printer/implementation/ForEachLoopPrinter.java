package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForEachLoop;

import jamopp.printer.interfaces.Printer;

class ForEachLoopPrinter implements Printer<ForEachLoop>{

	private final OrdinaryParameterPrinter OrdinaryParameterPrinter;
	private final ExpressionPrinter ExpressionPrinter;
	private final StatementPrinter StatementPrinter;
	
	public void print(ForEachLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		OrdinaryParameterPrinter.print(element.getNext(), writer);
		writer.append(" : ");
		ExpressionPrinter.print(element.getCollection(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
