package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class RelationExpressionPrinterImpl implements Printer<RelationExpression> {

	private final Printer<RelationExpressionChild> relationExpressionChildPrinter;
	private final Printer<RelationOperator> relationOperatorPrinter;

	@Inject
	public RelationExpressionPrinterImpl(Printer<RelationExpressionChild> relationExpressionChildPrinter,
			Printer<RelationOperator> relationOperatorPrinter) {
		this.relationExpressionChildPrinter = relationExpressionChildPrinter;
		this.relationOperatorPrinter = relationOperatorPrinter;
	}

	@Override
	public void print(RelationExpression element, BufferedWriter writer) throws IOException {
		this.relationExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			this.relationOperatorPrinter.print(element.getRelationOperators().get(index - 1), writer);
			this.relationExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
