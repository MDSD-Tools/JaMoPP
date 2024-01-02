package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.ForLoop;
import tools.mdsd.jamopp.model.java.statements.ForLoopInitializer;
import tools.mdsd.jamopp.model.java.statements.Statement;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ForLoopPrinterImpl implements Printer<ForLoop> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<ForLoopInitializer> forLoopInitializerPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public ForLoopPrinterImpl(Printer<ForLoopInitializer> forLoopInitializerPrinter,
			Printer<Expression> expressionPrinter, Printer<Statement> statementPrinter) {
		this.forLoopInitializerPrinter = forLoopInitializerPrinter;
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(ForLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		if (element.getInit() != null) {
			this.forLoopInitializerPrinter.print(element.getInit(), writer);
		}
		writer.append(" ; ");
		if (element.getCondition() != null) {
			this.expressionPrinter.print(element.getCondition(), writer);
		}
		writer.append(" ; ");
		for (var index = 0; index < element.getUpdates().size(); index++) {
			this.expressionPrinter.print(element.getUpdates().get(index), writer);
			if (index < element.getUpdates().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")\n");
		this.statementPrinter.print(element.getStatement(), writer);
	}

}
