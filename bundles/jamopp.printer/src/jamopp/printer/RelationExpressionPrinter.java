package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpression;

class RelationExpressionPrinter {

	static void print(RelationExpression element, BufferedWriter writer) throws IOException {
		RelationExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			RelationOperatorPrinter.print(element.getRelationOperators().get(index - 1), writer);
			RelationExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
