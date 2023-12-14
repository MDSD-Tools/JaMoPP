package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ForLoopInitializerPrinterInt;
import jamopp.printer.interfaces.printer.ForLoopPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class ForLoopPrinter implements ForLoopPrinterInt {

	private final ForLoopInitializerPrinterInt ForLoopInitializerPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public ForLoopPrinter(ForLoopInitializerPrinterInt forLoopInitializerPrinter,
			ExpressionPrinterInt expressionPrinter, StatementPrinterInt statementPrinter) {
		ForLoopInitializerPrinter = forLoopInitializerPrinter;
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(ForLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		if (element.getInit() != null) {
			ForLoopInitializerPrinter.print(element.getInit(), writer);
		}
		writer.append(" ; ");
		if (element.getCondition() != null) {
			ExpressionPrinter.print(element.getCondition(), writer);
		}
		writer.append(" ; ");
		for (int index = 0; index < element.getUpdates().size(); index++) {
			ExpressionPrinter.print(element.getUpdates().get(index), writer);
			if (index < element.getUpdates().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
