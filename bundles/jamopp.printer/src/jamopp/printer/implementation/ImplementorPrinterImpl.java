package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Implementor;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ImplementorPrinterImpl implements Printer<Implementor> {

	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public ImplementorPrinterImpl(Printer<TypeReference> typeReferencePrinter) {
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(Implementor element, BufferedWriter writer) throws IOException {
		if (!element.getImplements().isEmpty()) {
			writer.append("implements ");
			TypeReferencePrinter.print(element.getImplements().get(0), writer);
			for (var index = 1; index < element.getImplements().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.print(element.getImplements().get(index), writer);
			}
			writer.append(" ");
		}
	}



}
