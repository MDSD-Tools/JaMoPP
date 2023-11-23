package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExpressionList;
import org.emftext.language.java.statements.ForLoopInitializer;
import org.emftext.language.java.variables.LocalVariable;

public class ForLoopInitializerPrinter {

	static void printForLoopInitializer(ForLoopInitializer element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			LocalVariablePrinter.printLocalVariable((LocalVariable) element, writer);
		} else {
			ExpressionList list = (ExpressionList) element;
			for (int index = 0; index < list.getExpressions().size(); index++) {
				ExpressionPrinter.printExpression(list.getExpressions().get(index), writer);
				if (index < list.getExpressions().size() - 1) {
					writer.append(", ");
				}
			}
		}
	}

}
