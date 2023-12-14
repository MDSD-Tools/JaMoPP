package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.ExpressionStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ExpressionStatementPrinterImpl implements Printer<ExpressionStatement> {

	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public ExpressionStatementPrinterImpl(Printer<Expression> expressionPrinter) {
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ExpressionStatement element, BufferedWriter writer) throws IOException {
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(";\n");
	}



}
