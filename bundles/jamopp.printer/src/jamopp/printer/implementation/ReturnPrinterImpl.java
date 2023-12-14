package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Return;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ReturnPrinterInt;

public class ReturnPrinterImpl implements ReturnPrinterInt {

	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public ReturnPrinterImpl(Printer<Expression> expressionPrinter) {
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
