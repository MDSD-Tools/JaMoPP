package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Assert;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
