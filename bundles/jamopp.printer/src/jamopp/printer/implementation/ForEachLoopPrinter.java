package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForEachLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ForEachLoopPrinterInt;
import jamopp.printer.interfaces.printer.OrdinaryParameterPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class ForEachLoopPrinter implements ForEachLoopPrinterInt {

	private final OrdinaryParameterPrinterInt OrdinaryParameterPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public ForEachLoopPrinter(OrdinaryParameterPrinterInt ordinaryParameterPrinter,
			ExpressionPrinterInt expressionPrinter, StatementPrinterInt statementPrinter) {
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
