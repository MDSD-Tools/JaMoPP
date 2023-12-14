package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Throw;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ThrowPrinterInt;

public class ThrowPrinterImpl implements ThrowPrinterInt {

	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public ThrowPrinterImpl(Printer<Expression> expressionPrinter) {
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Throw element, BufferedWriter writer) throws IOException {
		writer.append("throw ");
		ExpressionPrinter.print(element.getThrowable(), writer);
		writer.append(";\n");
	}

}
