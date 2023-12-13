package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.EnumConstant;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.EnumConstantPrinterInt;

class EnumConstantPrinter implements EnumConstantPrinterInt {

	private final AnnotablePrinter AnnotablePrinter;
	private final ArgumentablePrinter ArgumentablePrinter;
	private final AnonymousClassPrinter AnonymousClassPrinter;

	@Inject
	public EnumConstantPrinter(jamopp.printer.implementation.AnnotablePrinter annotablePrinter,
			jamopp.printer.implementation.ArgumentablePrinter argumentablePrinter,
			jamopp.printer.implementation.AnonymousClassPrinter anonymousClassPrinter) {
		super();
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
