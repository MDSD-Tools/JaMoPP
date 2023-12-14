package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.ForEachLoop;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.OrdinaryParameterPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class ForEachLoopPrinterImpl implements Printer<ForEachLoop> {

	private final Printer<Expression> ExpressionPrinter;
	private final OrdinaryParameterPrinterInt OrdinaryParameterPrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public ForEachLoopPrinterImpl(OrdinaryParameterPrinterInt ordinaryParameterPrinter,
			Printer<Expression> expressionPrinter, StatementPrinterInt statementPrinter) {
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
