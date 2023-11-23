package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DoWhileLoop;

class DoWhileLoopPrinter {

	static void print(DoWhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("do\n");
		StatementPrinter.print(element.getStatement(), writer);
		writer.append("while (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(");\n");
	}

}
