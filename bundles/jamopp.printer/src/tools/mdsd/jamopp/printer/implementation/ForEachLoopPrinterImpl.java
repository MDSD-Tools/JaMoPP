package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.statements.ForEachLoop;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ForEachLoopPrinterImpl implements Printer<ForEachLoop> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<OrdinaryParameter> ordinaryParameterPrinter;
	private final Printer<Statement> statementPrinter;

	@Inject
	public ForEachLoopPrinterImpl(Printer<OrdinaryParameter> ordinaryParameterPrinter,
			Printer<Expression> expressionPrinter, Printer<Statement> statementPrinter) {
		this.ordinaryParameterPrinter = ordinaryParameterPrinter;
		this.expressionPrinter = expressionPrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(ForEachLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		this.ordinaryParameterPrinter.print(element.getNext(), writer);
		writer.append(" : ");
		this.expressionPrinter.print(element.getCollection(), writer);
		writer.append(")\n");
		this.statementPrinter.print(element.getStatement(), writer);
	}

}
