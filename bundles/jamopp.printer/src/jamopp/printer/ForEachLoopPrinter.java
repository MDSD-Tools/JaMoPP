package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForEachLoop;

public class ForEachLoopPrinter {

	static void printForEachLoop(ForEachLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		OrdinaryParameterPrinter.printOrdinaryParameter(element.getNext(), writer);
		writer.append(" : ");
		ExpressionPrinter.printExpression(element.getCollection(), writer);
		writer.append(")\n");
		StatementPrinter.printStatement(element.getStatement(), writer);
	}

}
