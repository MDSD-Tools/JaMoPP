package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.statements.LocalVariableStatement;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class LocalVariableStatementPrinterImpl implements Printer<LocalVariableStatement> {

	private final Printer<LocalVariable> localVariablePrinter;

	@Inject
	public LocalVariableStatementPrinterImpl(Printer<LocalVariable> localVariablePrinter) {
		this.localVariablePrinter = localVariablePrinter;
	}

	@Override
	public void print(LocalVariableStatement element, BufferedWriter writer) throws IOException {
		this.localVariablePrinter.print(element.getVariable(), writer);
		writer.append(";\n");
	}

}
