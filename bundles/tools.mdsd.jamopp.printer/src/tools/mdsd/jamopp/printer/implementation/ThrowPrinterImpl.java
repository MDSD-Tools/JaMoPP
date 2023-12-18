package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Throw;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ThrowPrinterImpl implements Printer<Throw> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public ThrowPrinterImpl(Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Throw element, BufferedWriter writer) throws IOException {
		writer.append("throw ");
		this.expressionPrinter.print(element.getThrowable(), writer);
		writer.append(";\n");
	}

}
