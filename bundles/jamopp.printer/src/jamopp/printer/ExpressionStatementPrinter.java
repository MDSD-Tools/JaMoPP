package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ExpressionStatement;

public class ExpressionStatementPrinter {

	static void printExpressionStatement(ExpressionStatement element, BufferedWriter writer)
			throws IOException {
		ExpressionPrinter.printExpression(element.getExpression(), writer);
		writer.append(";\n");
	}

}
