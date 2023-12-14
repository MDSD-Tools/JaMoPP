package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.statements.SwitchCase;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class SwitchPrinterImpl implements Printer<Switch> {

	private final Printer<Expression> ExpressionPrinter;
	private final Printer<SwitchCase> SwitchCasePrinter;

	@Inject
	public SwitchPrinterImpl(Printer<Expression> expressionPrinter, Printer<SwitchCase> switchCasePrinter) {
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
