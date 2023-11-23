package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.NormalSwitchCase;
import org.emftext.language.java.statements.Statement;

class NormalSwitchCasePrinter {

	static void print(NormalSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("case ");
		ExpressionPrinter.print(element.getCondition(), writer);
		for (Expression expr : element.getAdditionalConditions()) {
			writer.append(", ");
			ExpressionPrinter.print(expr, writer);
		}
		writer.append(": ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
	}

}
