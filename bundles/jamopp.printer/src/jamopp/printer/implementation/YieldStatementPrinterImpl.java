package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.YieldStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.YieldStatementPrinterInt;

public class YieldStatementPrinterImpl implements YieldStatementPrinterInt {

	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public YieldStatementPrinterImpl(Printer<Expression> expressionPrinter) {
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		ExpressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
