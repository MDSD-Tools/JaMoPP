package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ConditionalExpressionPrinterImpl implements Printer<ConditionalExpression> {

	private final Printer<ConditionalExpressionChild> ConditionalExpressionChildPrinter;
	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public ConditionalExpressionPrinterImpl(Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter,
			Printer<Expression> expressionPrinter) {
		ConditionalExpressionChildPrinter = conditionalExpressionChildPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ConditionalExpression element, BufferedWriter writer) throws IOException {
		ConditionalExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getExpressionIf() != null) {
			writer.append(" ? ");
			ExpressionPrinter.print(element.getExpressionIf(), writer);
			writer.append(" : ");
			ExpressionPrinter.print(element.getGeneralExpressionElse(), writer);
		}
	}

}
