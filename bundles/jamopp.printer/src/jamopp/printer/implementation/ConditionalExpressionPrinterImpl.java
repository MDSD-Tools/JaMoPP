package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ConditionalExpressionPrinterImpl implements Printer<ConditionalExpression> {

	private final Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public ConditionalExpressionPrinterImpl(Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter,
			Printer<Expression> expressionPrinter) {
		this.conditionalExpressionChildPrinter = conditionalExpressionChildPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ConditionalExpression element, BufferedWriter writer) throws IOException {
		this.conditionalExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getExpressionIf() != null) {
			writer.append(" ? ");
			this.expressionPrinter.print(element.getExpressionIf(), writer);
			writer.append(" : ");
			this.expressionPrinter.print(element.getGeneralExpressionElse(), writer);
		}
	}

}
