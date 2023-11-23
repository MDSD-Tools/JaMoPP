package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Throw;

public class ThrowPrinter {

	static void printThrow(Throw element, BufferedWriter writer) throws IOException {
		writer.append("throw ");
		ExpressionPrinter.printExpression(element.getThrowable(), writer);
		writer.append(";\n");
	}

}
