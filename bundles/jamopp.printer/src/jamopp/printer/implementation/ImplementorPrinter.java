package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Implementor;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ImplementorPrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class ImplementorPrinter implements ImplementorPrinterInt {

	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public ImplementorPrinter(TypeReferencePrinterInt typeReferencePrinter) {
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
