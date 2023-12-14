package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.types.ClassifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

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
