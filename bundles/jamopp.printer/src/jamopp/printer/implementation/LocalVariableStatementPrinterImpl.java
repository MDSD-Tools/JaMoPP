package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.LocalVariableStatement;
import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class LocalVariableStatementPrinterImpl implements Printer<LocalVariableStatement> {

	private final Printer<LocalVariable> LocalVariablePrinter;

	@Inject
	public LocalVariableStatementPrinterImpl(Printer<LocalVariable> localVariablePrinter) {
		LocalVariablePrinter = localVariablePrinter;
	}

	@Override
	public void print(LocalVariableStatement element, BufferedWriter writer) throws IOException {
		LocalVariablePrinter.print(element.getVariable(), writer);
		writer.append(";\n");
	}

}
