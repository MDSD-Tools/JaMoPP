package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.DoWhileLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class DoWhileLoopPrinterImpl implements Printer<DoWhileLoop> {

	private final Printer<Expression> ExpressionPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public DoWhileLoopPrinterImpl(StatementPrinterInt statementPrinter, Printer<Expression> expressionPrinter) {
		StatementPrinter = statementPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(DoWhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("do\n");
		StatementPrinter.print(element.getStatement(), writer);
		writer.append("while (");
		ExpressionPrinter.print(element.getCondition(), writer);
		writer.append(");\n");
	}

}
