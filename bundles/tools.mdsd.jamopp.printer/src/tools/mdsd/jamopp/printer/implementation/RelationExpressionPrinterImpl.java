package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class RelationExpressionPrinterImpl implements Printer<RelationExpression> {

	private final Printer<RelationExpressionChild> relationExpressionChildPrinter;
	private final Printer<RelationOperator> relationOperatorPrinter;

	@Inject
	public RelationExpressionPrinterImpl(final Printer<RelationExpressionChild> relationExpressionChildPrinter,
			final Printer<RelationOperator> relationOperatorPrinter) {
		this.relationExpressionChildPrinter = relationExpressionChildPrinter;
		this.relationOperatorPrinter = relationOperatorPrinter;
	}

	@Override
	public void print(final RelationExpression element, final BufferedWriter writer) throws IOException {
		relationExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			relationOperatorPrinter.print(element.getRelationOperators().get(index - 1), writer);
			relationExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
