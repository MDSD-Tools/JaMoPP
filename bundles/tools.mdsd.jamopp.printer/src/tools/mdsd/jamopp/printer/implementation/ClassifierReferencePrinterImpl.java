package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ClassifierReferencePrinterImpl implements Printer<ClassifierReference> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;

	@Inject
	public ClassifierReferencePrinterImpl(final Printer<Annotable> annotablePrinter,
			final Printer<TypeArgumentable> typeArgumentablePrinter) {
		this.annotablePrinter = annotablePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
	}

	@Override
	public void print(final ClassifierReference element, final BufferedWriter writer) throws IOException {
		annotablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		typeArgumentablePrinter.print(element, writer);
	}

}
