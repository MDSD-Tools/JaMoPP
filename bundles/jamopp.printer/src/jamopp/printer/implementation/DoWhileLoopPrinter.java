package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DoWhileLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class DoWhileLoopPrinter implements Printer<DoWhileLoop> {

	private final StatementPrinter StatementPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public DoWhileLoopPrinter(jamopp.printer.implementation.StatementPrinter statementPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
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
