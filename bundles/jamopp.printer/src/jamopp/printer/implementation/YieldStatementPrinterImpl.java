package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.YieldStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class YieldStatementPrinterImpl implements Printer<YieldStatement> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public YieldStatementPrinterImpl(Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		this.expressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
