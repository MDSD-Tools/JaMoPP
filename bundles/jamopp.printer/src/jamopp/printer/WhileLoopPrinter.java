package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.WhileLoop;

public class WhileLoopPrinter {

	static void printWhileLoop(WhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("while (");
		ExpressionPrinter.printExpression(element.getCondition(), writer);
		writer.append(")\n");
		StatementPrinter.printStatement(element.getStatement(), writer);
	}

}
