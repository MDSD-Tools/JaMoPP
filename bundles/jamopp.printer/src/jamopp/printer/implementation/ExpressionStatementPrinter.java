package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ExpressionStatement;

import jamopp.printer.interfaces.Printer;

class ExpressionStatementPrinter implements Printer<ExpressionStatement>{

	public void print(ExpressionStatement element, BufferedWriter writer)
			throws IOException {
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(";\n");
	}

}
