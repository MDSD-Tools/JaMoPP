package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.statements.SwitchCase;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.SwitchCasePrinterInt;
import jamopp.printer.interfaces.printer.SwitchPrinterInt;

public class SwitchPrinterImpl implements SwitchPrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;
	private final SwitchCasePrinterInt SwitchCasePrinter;

	@Inject
	public SwitchPrinterImpl(ExpressionPrinterInt expressionPrinter, SwitchCasePrinterInt switchCasePrinter) {
		ExpressionPrinter = expressionPrinter;
		SwitchCasePrinter = switchCasePrinter;
	}

	@Override
	public void print(Switch element, BufferedWriter writer) throws IOException {
		writer.append("switch (");
		ExpressionPrinter.print(element.getVariable(), writer);
		writer.append(") {\n");
		for (SwitchCase cas : element.getCases()) {
			SwitchCasePrinter.print(cas, writer);
		}
		writer.append("}\n");
	}

}
