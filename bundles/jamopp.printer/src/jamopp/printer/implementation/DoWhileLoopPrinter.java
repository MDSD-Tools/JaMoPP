package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DoWhileLoop;

import jamopp.printer.interfaces.Printer;

class DoWhileLoopPrinter implements Printer<DoWhileLoop>{

	public void print(DoWhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("do\n");
		StatementPrinter.print(element.getStatement(), writer);
		writer.append("while (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(");\n");
	}

}
