package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.Argumentable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ArgumentablePrinterInt;

class ArgumentablePrinter implements  ArgumentablePrinterInt {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ArgumentablePrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Argumentable element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (int index = 0; index < element.getArguments().size(); index++) {
			ExpressionPrinter.print(element.getArguments().get(index), writer);
			if (index < element.getArguments().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")");
	}

}
