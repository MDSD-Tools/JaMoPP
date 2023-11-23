package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Throw;

class ThrowPrinter {

	static void print(Throw element, BufferedWriter writer) throws IOException {
		writer.append("throw ");
		ExpressionPrinter.print(element.getThrowable(), writer);
		writer.append(";\n");
	}

}
