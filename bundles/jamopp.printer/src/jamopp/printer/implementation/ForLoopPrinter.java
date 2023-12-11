package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForLoop;

import jamopp.printer.interfaces.Printer;

public class ForLoopPrinter implements Printer<ForLoop>{

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
