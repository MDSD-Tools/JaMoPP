package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.references.Argumentable;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class EnumConstantPrinterImpl implements Printer<EnumConstant> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<AnonymousClass> anonymousClassPrinter;
	private final Printer<Argumentable> argumentablePrinter;

	@Inject
	public EnumConstantPrinterImpl(Printer<Annotable> annotablePrinter, Printer<Argumentable> argumentablePrinter,
			Printer<AnonymousClass> anonymousClassPrinter) {
		this.annotablePrinter = annotablePrinter;
		this.argumentablePrinter = argumentablePrinter;
		this.anonymousClassPrinter = anonymousClassPrinter;
	}

	@Override
	public void print(EnumConstant element, BufferedWriter writer) throws IOException {
		this.annotablePrinter.print(element, writer);
		writer.append(element.getName() + " ");
		if (!element.getArguments().isEmpty()) {
			this.argumentablePrinter.print(element, writer);
		}
		if (element.getAnonymousClass() != null) {
			this.anonymousClassPrinter.print(element.getAnonymousClass(), writer);
		}
	}

}
