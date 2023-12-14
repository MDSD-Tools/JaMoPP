package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.TypeArgumentable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class TypeArgumentablePrinterImpl implements Printer<TypeArgumentable> {

	private final Printer<TypeArgument> TypeArgumentPrinter;

	@Inject
	public TypeArgumentablePrinterImpl(Printer<TypeArgument> typeArgumentPrinter) {
		TypeArgumentPrinter = typeArgumentPrinter;
	}

	@Override
	public void print(TypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeArguments().isEmpty()) {
			writer.append("<");
			TypeArgumentPrinter.print(element.getTypeArguments().get(0), writer);
			for (var index = 1; index < element.getTypeArguments().size(); index++) {
				writer.append(", ");
				TypeArgumentPrinter.print(element.getTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
