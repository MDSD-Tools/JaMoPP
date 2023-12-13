package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Implementor;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ImplementorPrinterInt;

class ImplementorPrinter implements ImplementorPrinterInt {

	private final TypeReferencePrinter TypeReferencePrinter;

	@Inject
	public ImplementorPrinter(jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter) {
		super();
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(Implementor element, BufferedWriter writer) throws IOException {
		if (!element.getImplements().isEmpty()) {
			writer.append("implements ");
			TypeReferencePrinter.print(element.getImplements().get(0), writer);
			for (int index = 1; index < element.getImplements().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.print(element.getImplements().get(index), writer);
			}
			writer.append(" ");
		}
	}

}
