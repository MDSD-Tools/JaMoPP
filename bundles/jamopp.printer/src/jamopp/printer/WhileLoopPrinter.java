package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.WhileLoop;

class WhileLoopPrinter {

	static void print(WhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("while (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
