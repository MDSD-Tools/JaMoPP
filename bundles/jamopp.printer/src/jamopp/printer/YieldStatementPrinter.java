package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.YieldStatement;

public class YieldStatementPrinter {

	static void printYieldStatement(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		ExpressionPrinter.printExpression(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
