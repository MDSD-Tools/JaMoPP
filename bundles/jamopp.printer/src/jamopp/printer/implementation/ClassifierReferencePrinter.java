package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.ClassifierReferencePrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;

public class ClassifierReferencePrinter implements ClassifierReferencePrinterInt {

	private final AnnotablePrinterInt AnnotablePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;

	@Inject
	public ClassifierReferencePrinter(AnnotablePrinterInt annotablePrinter,
			TypeArgumentablePrinterInt typeArgumentablePrinter) {
		AnnotablePrinter = annotablePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
	}

	@Override
	public void print(ClassifierReference element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		TypeArgumentablePrinter.print(element, writer);
	}



}
