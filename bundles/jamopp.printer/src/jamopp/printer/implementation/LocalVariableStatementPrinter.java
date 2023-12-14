package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.LocalVariableStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.LocalVariablePrinterInt;
import jamopp.printer.interfaces.printer.LocalVariableStatementPrinterInt;

public class LocalVariableStatementPrinter implements LocalVariableStatementPrinterInt {

	private final LocalVariablePrinterInt LocalVariablePrinter;

	@Inject
	public LocalVariableStatementPrinter(LocalVariablePrinterInt localVariablePrinter) {
		LocalVariablePrinter = localVariablePrinter;
	}

	@Override
	public void print(LocalVariableStatement element, BufferedWriter writer) throws IOException {
		LocalVariablePrinter.print(element.getVariable(), writer);
		writer.append(";\n");
	}

}
