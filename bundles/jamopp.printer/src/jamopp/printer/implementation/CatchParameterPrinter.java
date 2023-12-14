package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.CatchParameterPrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class CatchParameterPrinter implements CatchParameterPrinterInt {

	private final AnnotableAndModifiablePrinterInt AnnotableAndModifiablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
public CatchParameterPrinter(AnnotableAndModifiablePrinterInt annotableAndModifiablePrinter,
			TypeReferencePrinterInt typeReferencePrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(CatchParameter element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		if (!element.getTypeReferences().isEmpty()) {
			for (TypeReference ref : element.getTypeReferences()) {
				writer.append(" | ");
				TypeReferencePrinter.print(ref, writer);
			}
		}
		writer.append(" " + element.getName());
	}

	

}
