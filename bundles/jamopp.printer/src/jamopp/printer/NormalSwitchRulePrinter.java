package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.NormalSwitchRule;
import org.emftext.language.java.statements.Statement;

public class NormalSwitchRulePrinter {

	static void printNormalSwitchRule(NormalSwitchRule element, BufferedWriter writer) throws IOException {
		writer.append("case ");
		ExpressionPrinter.printExpression(element.getCondition(), writer);
		for (Expression expr : element.getAdditionalConditions()) {
			writer.append(", ");
			ExpressionPrinter.printExpression(expr, writer);
		}
		writer.append(" -> ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.printStatement(s, writer);
		}
	}

}
