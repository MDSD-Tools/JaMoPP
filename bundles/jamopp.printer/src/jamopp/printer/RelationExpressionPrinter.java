package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpression;

public class RelationExpressionPrinter {

	static void printRelationExpression(RelationExpression element, BufferedWriter writer) throws IOException {
		RelationExpressionChildPrinter.printRelationExpressionChild(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			RelationOperatorPrinter.printRelationOperator(element.getRelationOperators().get(index - 1), writer);
			RelationExpressionChildPrinter.printRelationExpressionChild(element.getChildren().get(index), writer);
		}
	}

}
