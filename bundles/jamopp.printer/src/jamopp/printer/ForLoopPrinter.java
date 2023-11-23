package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForLoop;

public class ForLoopPrinter {

	static void printForLoop(ForLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		if (element.getInit() != null) {
			ForLoopInitializerPrinter.printForLoopInitializer(element.getInit(), writer);
		}
		writer.append(" ; ");
		if (element.getCondition() != null) {
			ExpressionPrinter.printExpression(element.getCondition(), writer);
		}
		writer.append(" ; ");
		for (int index = 0; index < element.getUpdates().size(); index++) {
			ExpressionPrinter.printExpression(element.getUpdates().get(index), writer);
			if (index < element.getUpdates().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")\n");
		StatementPrinter.printStatement(element.getStatement(), writer);
	}

}
