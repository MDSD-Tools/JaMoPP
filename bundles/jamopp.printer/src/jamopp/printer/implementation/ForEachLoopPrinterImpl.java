package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.statements.ForEachLoop;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ForEachLoopPrinterImpl implements Printer<ForEachLoop> {

	private final Printer<Expression> ExpressionPrinter;
	private final Printer<OrdinaryParameter> OrdinaryParameterPrinter;
	private final Printer<Statement> StatementPrinter;

	@Inject
	public ForEachLoopPrinterImpl(Printer<OrdinaryParameter> ordinaryParameterPrinter,
			Printer<Expression> expressionPrinter, Printer<Statement> statementPrinter) {
		OrdinaryParameterPrinter = ordinaryParameterPrinter;
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(ForEachLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		OrdinaryParameterPrinter.print(element.getNext(), writer);
		writer.append(" : ");
		ExpressionPrinter.print(element.getCollection(), writer);
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
