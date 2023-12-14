package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.ForLoop;
import org.emftext.language.java.statements.ForLoopInitializer;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ForLoopPrinterImpl implements Printer<ForLoop> {

	private final Printer<Expression> ExpressionPrinter;
	private final Printer<ForLoopInitializer> ForLoopInitializerPrinter;
	private final Printer<Statement> StatementPrinter;

	@Inject
	public ForLoopPrinterImpl(Printer<ForLoopInitializer> forLoopInitializerPrinter,
			Printer<Expression> expressionPrinter, Printer<Statement> statementPrinter) {
		ForLoopInitializerPrinter = forLoopInitializerPrinter;
		ExpressionPrinter = expressionPrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(ForLoop element, BufferedWriter writer) throws IOException {
		writer.append("for (");
		if (element.getInit() != null) {
			ForLoopInitializerPrinter.print(element.getInit(), writer);
		}
		writer.append(" ; ");
		if (element.getCondition() != null) {
			ExpressionPrinter.print(element.getCondition(), writer);
		}
		writer.append(" ; ");
		for (var index = 0; index < element.getUpdates().size(); index++) {
			ExpressionPrinter.print(element.getUpdates().get(index), writer);
			if (index < element.getUpdates().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")\n");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
