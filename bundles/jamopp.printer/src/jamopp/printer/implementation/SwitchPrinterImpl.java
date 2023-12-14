package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.statements.SwitchCase;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.SwitchCasePrinterInt;
import jamopp.printer.interfaces.printer.SwitchPrinterInt;

public class SwitchPrinterImpl implements SwitchPrinterInt {

	private final Printer<Expression> ExpressionPrinter;
	private final SwitchCasePrinterInt SwitchCasePrinter;

	@Inject
	public SwitchPrinterImpl(Printer<Expression> expressionPrinter, SwitchCasePrinterInt switchCasePrinter) {
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
