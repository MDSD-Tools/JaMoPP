package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Assert;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AssertPrinterImpl implements Printer<Assert> {

	private final Provider<Printer<Expression>> expressionPrinter;

	@Inject
	public AssertPrinterImpl(final Provider<Printer<Expression>> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final Assert element, final BufferedWriter writer) throws IOException {
		writer.append("assert ");
		expressionPrinter.get().print(element.getCondition(), writer);
		if (element.getErrorMessage() != null) {
			writer.append(" : ");
			expressionPrinter.get().print(element.getErrorMessage(), writer);
		}
		writer.append(";\n");
	}

}
