package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForEachLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ForEachLoopPrinter implements Printer<ForEachLoop> {

	private final OrdinaryParameterPrinter OrdinaryParameterPrinter;
	private final ExpressionPrinter ExpressionPrinter;
	private final StatementPrinter StatementPrinter;

	@Inject
	public ForEachLoopPrinter(jamopp.printer.implementation.OrdinaryParameterPrinter ordinaryParameterPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter,
			jamopp.printer.implementation.StatementPrinter statementPrinter) {
		super();
		OrdinaryParameterPrinter = ordinaryParameterPrinter;
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(ForEachLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		OrdinaryParameterPrinter.print(element.getNext(), writer);
		writer.append(" : ");
		ExpressionPrinter.print(element.getCollection(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
