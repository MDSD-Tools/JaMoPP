package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.YieldStatement;

class YieldStatementPrinter {

	static void print(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		ExpressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
