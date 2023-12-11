package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.statements.SwitchCase;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class SwitchPrinter implements Printer<Switch> {

	private final ExpressionPrinter ExpressionPrinter;
	private final SwitchCasePrinter SwitchCasePrinter;

	@Inject
	public SwitchPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter,
			jamopp.printer.implementation.SwitchCasePrinter switchCasePrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
		SwitchCasePrinter = switchCasePrinter;
	}

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
