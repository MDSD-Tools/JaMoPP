package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ClassifierReferencePrinterImpl implements Printer<ClassifierReference> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;

	@Inject
	public ClassifierReferencePrinterImpl(Printer<Annotable> annotablePrinter,
			Printer<TypeArgumentable> typeArgumentablePrinter) {
		this.annotablePrinter = annotablePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
	}

	@Override
	public void print(ClassifierReference element, BufferedWriter writer) throws IOException {
		this.annotablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		this.typeArgumentablePrinter.print(element, writer);
	}

}
