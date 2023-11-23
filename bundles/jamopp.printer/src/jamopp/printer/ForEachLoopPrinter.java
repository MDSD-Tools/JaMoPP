package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ForEachLoop;

class ForEachLoopPrinter {

	static void print(ForEachLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		OrdinaryParameterPrinter.print(element.getNext(), writer);
		writer.append(" : ");
		ExpressionPrinter.print(element.getCollection(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
