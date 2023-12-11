package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ClassifierReferencePrinter implements Printer<ClassifierReference> {

	private final AnnotablePrinter AnnotablePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;

	@Inject
	public ClassifierReferencePrinter(jamopp.printer.implementation.AnnotablePrinter annotablePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter) {
		super();
		AnnotablePrinter = annotablePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
	}

	public void print(ClassifierReference element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		TypeArgumentablePrinter.print(element, writer);
	}

}
