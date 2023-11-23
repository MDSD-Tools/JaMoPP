package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DoWhileLoop;

public class DoWhileLoopPrinter {

	static void printDoWhileLoop(DoWhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("do\n");
		StatementPrinter.printStatement(element.getStatement(), writer);
		writer.append("while (");
		ExpressionPrinter.printExpression(element.getCondition(), writer);
		writer.append(");\n");
	}

}
