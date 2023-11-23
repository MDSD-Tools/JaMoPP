package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.statements.SwitchCase;

class SwitchPrint {

	static void print(Switch element, BufferedWriter writer) throws IOException {
		writer.append("switch (");
		ExpressionPrinter.print(element.getVariable(), writer);
		writer.append(") {\n");
		for (SwitchCase cas : element.getCases()) {
			SwitchCasePrinter.print(cas, writer);
		}
		writer.append("}\n");
	}

}
