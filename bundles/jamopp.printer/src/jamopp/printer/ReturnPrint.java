package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Return;

public class ReturnPrint {

	static void printReturn(Return element, BufferedWriter writer) throws IOException {
		writer.append("return");
		if (element.getReturnValue() != null) {
			writer.append(" ");
			ExpressionPrinter.printExpression(element.getReturnValue(), writer);
		}
		writer.append(";\n");
	}

}
