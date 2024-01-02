package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.references.Argumentable;

import javax.inject.Inject;

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
