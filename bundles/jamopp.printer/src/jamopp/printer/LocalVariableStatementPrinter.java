package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.LocalVariableStatement;

public class LocalVariableStatementPrinter {

	static void printLocalVariableStatement(LocalVariableStatement element, BufferedWriter writer)
			throws IOException {
		LocalVariablePrinter.printLocalVariable(element.getVariable(), writer);
		writer.append(";\n");
	}

}
