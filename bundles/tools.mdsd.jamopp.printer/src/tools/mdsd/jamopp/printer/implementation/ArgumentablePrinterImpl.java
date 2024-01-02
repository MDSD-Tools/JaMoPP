package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.references.Argumentable;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ArgumentablePrinterImpl implements Printer<Argumentable> {

	private final Printer<Expression> expressionPrinter;

	@Inject
	public ArgumentablePrinterImpl(Printer<Expression> expressionPrinter) {
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Argumentable element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (var index = 0; index < element.getArguments().size(); index++) {
			this.expressionPrinter.print(element.getArguments().get(index), writer);
			if (index < element.getArguments().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")");
	}

}
