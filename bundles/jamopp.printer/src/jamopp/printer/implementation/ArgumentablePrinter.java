package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.Argumentable;

import jamopp.printer.interfaces.Printer;

class ArgumentablePrinter implements Printer<Argumentable> {

	private final ExpressionPrinter ExpressionPrinter;
	
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
