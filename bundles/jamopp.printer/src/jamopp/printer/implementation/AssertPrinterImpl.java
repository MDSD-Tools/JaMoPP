package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Assert;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.printer.interfaces.printer.AssertPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;

public class AssertPrinterImpl implements AssertPrinterInt {

	private final Provider<ExpressionPrinterInt> ExpressionPrinter;

	@Inject
	public AssertPrinterImpl(Provider<ExpressionPrinterInt> expressionPrinter) {
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Assert element, BufferedWriter writer) throws IOException {
		writer.append("assert ");
		ExpressionPrinter.get().print(element.getCondition(), writer);
		if (element.getErrorMessage() != null) {
			writer.append(" : ");
			ExpressionPrinter.get().print(element.getErrorMessage(), writer);
		}
		writer.append(";\n");
	}

	

}
