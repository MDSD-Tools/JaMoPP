package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Condition;

class ConditionPrinter {

	static void print(Condition element, BufferedWriter writer) throws IOException {
		writer.append("if (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
		if (element.getElseStatement() != null) {
			writer.append("else\n");
			StatementPrinter.print(element.getElseStatement(), writer);
		}
	}

}
