package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Assert;

class AssertPrinter {

	static void print(Assert element, BufferedWriter writer) throws IOException {
		writer.append("assert ");
		ExpressionPrinter.print(element.getCondition(), writer);
		if (element.getErrorMessage() != null) {
			writer.append(" : ");
			ExpressionPrinter.print(element.getErrorMessage(), writer);
		}
		writer.append(";\n");
	}

}
