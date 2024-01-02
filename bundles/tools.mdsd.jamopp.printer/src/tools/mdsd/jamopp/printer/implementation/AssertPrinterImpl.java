package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Assert;

import javax.inject.Inject;
import javax.inject.Provider;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AssertPrinterImpl implements Printer<Assert> {

	private final Provider<Printer<Expression>> expressionPrinter;

	@Inject
	public AssertPrinterImpl(Provider<Printer<Expression>> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Assert element, BufferedWriter writer) throws IOException {
		writer.append("assert ");
		this.expressionPrinter.get().print(element.getCondition(), writer);
		if (element.getErrorMessage() != null) {
			writer.append(" : ");
			this.expressionPrinter.get().print(element.getErrorMessage(), writer);
		}
		writer.append(";\n");
	}

}
