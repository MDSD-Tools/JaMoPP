package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.LocalVariableStatement;

class LocalVariableStatementPrinter {

	static void print(LocalVariableStatement element, BufferedWriter writer)
			throws IOException {
		LocalVariablePrinter.print(element.getVariable(), writer);
		writer.append(";\n");
	}

}
