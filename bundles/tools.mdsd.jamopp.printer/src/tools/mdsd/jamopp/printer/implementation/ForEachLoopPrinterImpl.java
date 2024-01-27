package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.statements.ForEachLoop;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ForEachLoopPrinterImpl implements Printer<ForEachLoop> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<OrdinaryParameter> ordinaryParameterPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public ForEachLoopPrinterImpl(final Printer<OrdinaryParameter> ordinaryParameterPrinter,
			final Printer<Expression> expressionPrinter, final Printer<Statement> statementPrinter) {
		this.ordinaryParameterPrinter = ordinaryParameterPrinter;
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final ForEachLoop element, final BufferedWriter writer) throws IOException {
		writer.append("for (");
		ordinaryParameterPrinter.print(element.getNext(), writer);
		writer.append(" : ");
		expressionPrinter.print(element.getCollection(), writer);
		writer.append(")\n");
		statementPrinter.print(element.getStatement(), writer);
	}

}
