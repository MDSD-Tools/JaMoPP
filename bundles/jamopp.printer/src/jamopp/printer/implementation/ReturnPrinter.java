package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Return;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ReturnPrinterInt;

class ReturnPrinter implements Printer<Return>, ReturnPrinterInt {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ReturnPrinter(ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Return element, BufferedWriter writer) throws IOException {
		writer.append("return");
		if (element.getReturnValue() != null) {
			writer.append(" ");
			ExpressionPrinter.print(element.getReturnValue(), writer);
		}
		writer.append(";\n");
	}

}
