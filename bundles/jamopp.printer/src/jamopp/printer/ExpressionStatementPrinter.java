package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ExpressionStatement;

class ExpressionStatementPrinter {

	static void print(ExpressionStatement element, BufferedWriter writer)
			throws IOException {
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(";\n");
	}

}
