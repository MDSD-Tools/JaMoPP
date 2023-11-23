package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Condition;

public class ConditionPrinter {

	static void printCondition(Condition element, BufferedWriter writer) throws IOException {
		writer.append("if (");
		ExpressionPrinter.printExpression(element.getCondition(), writer);
		writer.append(")\n");
		StatementPrinter.printStatement(element.getStatement(), writer);
		if (element.getElseStatement() != null) {
			writer.append("else\n");
			StatementPrinter.printStatement(element.getElseStatement(), writer);
		}
	}

}
