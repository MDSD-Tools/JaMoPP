package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpression;

import jamopp.printer.interfaces.Printer;

class RelationExpressionPrinter implements Printer<RelationExpression> {

	public void print(RelationExpression element, BufferedWriter writer) throws IOException {
		RelationExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			RelationOperatorPrinter.print(element.getRelationOperators().get(index - 1), writer);
			RelationExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
