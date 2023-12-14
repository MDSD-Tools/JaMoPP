package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Return;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ReturnPrinterImpl implements Printer<Return> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public ReturnPrinterImpl(Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Return element, BufferedWriter writer) throws IOException {
		writer.append("return");
		if (element.getReturnValue() != null) {
			writer.append(" ");
			this.expressionPrinter.print(element.getReturnValue(), writer);
		}
		writer.append(";\n");
	}

}
