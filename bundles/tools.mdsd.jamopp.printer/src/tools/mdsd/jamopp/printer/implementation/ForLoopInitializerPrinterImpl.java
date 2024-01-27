package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.ExpressionList;
import tools.mdsd.jamopp.model.java.statements.ForLoopInitializer;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ForLoopInitializerPrinterImpl implements Printer<ForLoopInitializer> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<LocalVariable> localVariablePrinter;

	@Inject
	public ForLoopInitializerPrinterImpl(final Printer<LocalVariable> localVariablePrinter,
			final Printer<Expression> expressionPrinter) {
		this.localVariablePrinter = localVariablePrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final ForLoopInitializer element, final BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			localVariablePrinter.print((LocalVariable) element, writer);
		} else {
			final var list = (ExpressionList) element;
			for (var index = 0; index < list.getExpressions().size(); index++) {
				expressionPrinter.print(list.getExpressions().get(index), writer);
				if (index < list.getExpressions().size() - 1) {
					writer.append(", ");
				}
			}
		}
	}

}
