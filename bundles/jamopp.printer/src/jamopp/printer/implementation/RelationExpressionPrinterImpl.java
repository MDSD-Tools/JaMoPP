package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.operators.RelationOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class RelationExpressionPrinterImpl implements Printer<RelationExpression> {

	private final Printer<RelationExpressionChild> RelationExpressionChildPrinter;
	private final Printer<RelationOperator> RelationOperatorPrinter;

	@Inject
	public RelationExpressionPrinterImpl(Printer<RelationExpressionChild> relationExpressionChildPrinter,
			Printer<RelationOperator> relationOperatorPrinter) {
		RelationExpressionChildPrinter = relationExpressionChildPrinter;
		RelationOperatorPrinter = relationOperatorPrinter;
	}

	@Override
	public void print(RelationExpression element, BufferedWriter writer) throws IOException {
		RelationExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			RelationOperatorPrinter.print(element.getRelationOperators().get(index - 1), writer);
			RelationExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
