package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Return;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ReturnPrinterImpl implements Printer<Return> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public ReturnPrinterImpl(final Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final Return element, final BufferedWriter writer) throws IOException {
		writer.append("return");
		if (element.getReturnValue() != null) {
			writer.append(" ");
			expressionPrinter.print(element.getReturnValue(), writer);
		}
		writer.append(";\n");
	}

}
