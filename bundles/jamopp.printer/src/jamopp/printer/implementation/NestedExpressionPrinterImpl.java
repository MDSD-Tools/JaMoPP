package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.NestedExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class NestedExpressionPrinterImpl implements Printer<NestedExpression> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public NestedExpressionPrinterImpl(Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(NestedExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		this.expressionPrinter.print(element.getExpression(), writer);
		writer.append(")");
	}

}
