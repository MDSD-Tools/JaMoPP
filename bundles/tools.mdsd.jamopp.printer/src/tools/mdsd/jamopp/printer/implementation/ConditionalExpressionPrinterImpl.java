package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ConditionalExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalExpressionPrinterImpl implements Printer<ConditionalExpression> {

	private final Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public ConditionalExpressionPrinterImpl(final Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter,
			final Printer<Expression> expressionPrinter) {
		this.conditionalExpressionChildPrinter = conditionalExpressionChildPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final ConditionalExpression element, final BufferedWriter writer) throws IOException {
		conditionalExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getExpressionIf() != null) {
			writer.append(" ? ");
			expressionPrinter.print(element.getExpressionIf(), writer);
			writer.append(" : ");
			expressionPrinter.print(element.getGeneralExpressionElse(), writer);
		}
	}

}
