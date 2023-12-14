package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.references.Argumentable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class EnumConstantPrinterImpl implements Printer<EnumConstant> {

	private final Printer<Annotable> AnnotablePrinter;
	private final Printer<AnonymousClass> AnonymousClassPrinter;
	private final Printer<Argumentable> ArgumentablePrinter;

	@Inject
	public EnumConstantPrinterImpl(Printer<Annotable> annotablePrinter, Printer<Argumentable> argumentablePrinter,
			Printer<AnonymousClass> anonymousClassPrinter) {
		AnnotablePrinter = annotablePrinter;
		ArgumentablePrinter = argumentablePrinter;
		AnonymousClassPrinter = anonymousClassPrinter;
	}

	@Override
	public void print(EnumConstant element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append(element.getName() + " ");
		if (!element.getArguments().isEmpty()) {
			ArgumentablePrinter.print(element, writer);
		}
		if (element.getAnonymousClass() != null) {
			AnonymousClassPrinter.print(element.getAnonymousClass(), writer);
		}
	}

}
