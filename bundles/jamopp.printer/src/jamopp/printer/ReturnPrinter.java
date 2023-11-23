package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Return;

class ReturnPrinter {

	static void print(Return element, BufferedWriter writer) throws IOException {
		writer.append("return");
		if (element.getReturnValue() != null) {
			writer.append(" ");
			ExpressionPrinter.print(element.getReturnValue(), writer);
		}
		writer.append(";\n");
	}

}
