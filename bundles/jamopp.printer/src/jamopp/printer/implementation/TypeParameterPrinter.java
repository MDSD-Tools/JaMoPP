package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.TypeParameterPrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class TypeParameterPrinter implements TypeParameterPrinterInt {

	private final AnnotablePrinterInt AnnotablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public TypeParameterPrinter(AnnotablePrinterInt annotablePrinter, TypeReferencePrinterInt typeReferencePrinter) {
		AnnotablePrinter = annotablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(TypeParameter element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append(element.getName());
		if (!element.getExtendTypes().isEmpty()) {
			writer.append(" extends ");
			TypeReferencePrinter.print(element.getExtendTypes().get(0), writer);
			for (int index = 1; index < element.getExtendTypes().size(); index++) {
				writer.append(" & ");
				TypeReferencePrinter.print(element.getExtendTypes().get(index), writer);
			}
		}
	}

}
