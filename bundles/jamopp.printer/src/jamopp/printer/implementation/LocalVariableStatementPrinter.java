package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.LocalVariableStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class LocalVariableStatementPrinter implements Printer<LocalVariableStatement> {

	private final LocalVariablePrinter LocalVariablePrinter;

	@Inject
	public LocalVariableStatementPrinter(jamopp.printer.implementation.LocalVariablePrinter localVariablePrinter) {
		super();
		LocalVariablePrinter = localVariablePrinter;
	}

	public void print(LocalVariableStatement element, BufferedWriter writer) throws IOException {
		LocalVariablePrinter.print(element.getVariable(), writer);
		writer.append(";\n");
	}

}
