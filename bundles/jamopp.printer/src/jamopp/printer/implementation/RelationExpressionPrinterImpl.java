package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.RelationExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.RelationExpressionPrinterInt;
import jamopp.printer.interfaces.printer.RelationOperatorPrinterInt;

public class RelationExpressionPrinterImpl implements RelationExpressionPrinterInt {

	private final RelationExpressionChildPrinterInt RelationExpressionChildPrinter;
	private final RelationOperatorPrinterInt RelationOperatorPrinter;

	@Inject
	public RelationExpressionPrinterImpl(RelationExpressionChildPrinterInt relationExpressionChildPrinter,
			RelationOperatorPrinterInt relationOperatorPrinter) {
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
