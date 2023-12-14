package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.DoWhileLoop;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class DoWhileLoopPrinterImpl implements Printer<DoWhileLoop> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public DoWhileLoopPrinterImpl(Printer<Statement> statementPrinter, Printer<Expression> expressionPrinter) {
		this.statementPrinter = statementPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(DoWhileLoop element, BufferedWriter writer) throws IOException {
		writer.append("do\n");
		this.statementPrinter.print(element.getStatement(), writer);
		writer.append("while (");
		this.expressionPrinter.print(element.getCondition(), writer);
		writer.append(");\n");
	}

}
