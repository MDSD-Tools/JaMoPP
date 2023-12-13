package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.RelationExpressionPrinterInt;

public class RelationExpressionPrinter implements RelationExpressionPrinterInt {

	private final RelationExpressionChildPrinter RelationExpressionChildPrinter;
	private final RelationOperatorPrinter RelationOperatorPrinter;

	@Inject
	public RelationExpressionPrinter(
			jamopp.printer.implementation.RelationExpressionChildPrinter relationExpressionChildPrinter,
			jamopp.printer.implementation.RelationOperatorPrinter relationOperatorPrinter) {
		super();
		RelationExpressionChildPrinter = relationExpressionChildPrinter;
		RelationOperatorPrinter = relationOperatorPrinter;
	}

	@Override
	public void print(RelationExpression element, BufferedWriter writer) throws IOException {
		RelationExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			RelationOperatorPrinter.print(element.getRelationOperators().get(index - 1), writer);
			RelationExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
