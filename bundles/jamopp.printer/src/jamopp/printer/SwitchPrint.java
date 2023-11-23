package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Switch;
import org.emftext.language.java.statements.SwitchCase;

public class SwitchPrint {

	static void printSwitch(Switch element, BufferedWriter writer) throws IOException {
		writer.append("switch (");
		ExpressionPrinter.printExpression(element.getVariable(), writer);
		writer.append(") {\n");
		for (SwitchCase cas : element.getCases()) {
			SwitchCasePrinter.printSwitchCase(cas, writer);
		}
		writer.append("}\n");
	}

}
