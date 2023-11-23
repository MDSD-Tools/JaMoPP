package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Assert;

public class AssertPrinter {

	static void printAssert(Assert element, BufferedWriter writer) throws IOException {
		writer.append("assert ");
		ExpressionPrinter.printExpression(element.getCondition(), writer);
		if (element.getErrorMessage() != null) {
			writer.append(" : ");
			ExpressionPrinter.printExpression(element.getErrorMessage(), writer);
		}
		writer.append(";\n");
	}

}
