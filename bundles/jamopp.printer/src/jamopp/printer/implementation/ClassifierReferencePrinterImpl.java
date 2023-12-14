package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.types.ClassifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ClassifierReferencePrinterImpl implements Printer<ClassifierReference> {

	private final Printer<Annotable> AnnotablePrinter;
	private final Printer<TypeArgumentable> TypeArgumentablePrinter;

	@Inject
	public ClassifierReferencePrinterImpl(Printer<Annotable> annotablePrinter,
			Printer<TypeArgumentable> typeArgumentablePrinter) {
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
