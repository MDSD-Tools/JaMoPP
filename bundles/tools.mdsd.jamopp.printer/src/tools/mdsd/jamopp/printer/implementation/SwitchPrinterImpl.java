package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.model.java.statements.SwitchCase;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SwitchPrinterImpl implements Printer<Switch> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<SwitchCase> switchCasePrinter;

	@Inject
	public SwitchPrinterImpl(final Printer<Expression> expressionPrinter, final Printer<SwitchCase> switchCasePrinter) {
		this.expressionPrinter = expressionPrinter;
		this.switchCasePrinter = switchCasePrinter;
	}

	@Override
	public void print(final Switch element, final BufferedWriter writer) throws IOException {
		writer.append("switch (");
		expressionPrinter.print(element.getVariable(), writer);
		writer.append(") {\n");
		for (final SwitchCase cas : element.getCases()) {
			switchCasePrinter.print(cas, writer);
		}
		writer.append("}\n");
	}

}
