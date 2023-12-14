package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeArgumentable;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.TypeArgumentPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;

public class TypeArgumentablePrinterImpl implements TypeArgumentablePrinterInt {

	private final TypeArgumentPrinterInt TypeArgumentPrinter;

	@Inject
	public TypeArgumentablePrinterImpl(TypeArgumentPrinterInt typeArgumentPrinter) {
		TypeArgumentPrinter = typeArgumentPrinter;
	}

	@Override
	public void print(TypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeArguments().isEmpty()) {
			writer.append("<");
			TypeArgumentPrinter.print(element.getTypeArguments().get(0), writer);
			for (int index = 1; index < element.getTypeArguments().size(); index++) {
				writer.append(", ");
				TypeArgumentPrinter.print(element.getTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
