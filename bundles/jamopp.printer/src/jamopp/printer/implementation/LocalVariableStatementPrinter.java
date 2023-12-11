package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.LocalVariableStatement;

import jamopp.printer.interfaces.Printer;

class LocalVariableStatementPrinter implements Printer<LocalVariableStatement> {

	private final LocalVariablePrinter LocalVariablePrinter;
	
	public void print(LocalVariableStatement element, BufferedWriter writer) throws IOException {
		LocalVariablePrinter.print(element.getVariable(), writer);
		writer.append(";\n");
	}

}
