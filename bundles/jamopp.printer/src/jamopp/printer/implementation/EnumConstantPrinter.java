package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.EnumConstant;

import jamopp.printer.interfaces.Printer;

class EnumConstantPrinter implements Printer<EnumConstant>{

	private final AnnotablePrinter AnnotablePrinter;
	private final ArgumentablePrinter ArgumentablePrinter;
	private final AnonymousClassPrinter AnonymousClassPrinter;
	
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
