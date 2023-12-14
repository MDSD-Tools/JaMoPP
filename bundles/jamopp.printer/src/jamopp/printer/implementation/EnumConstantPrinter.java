package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.EnumConstant;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.AnonymousClassPrinterInt;
import jamopp.printer.interfaces.printer.ArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.EnumConstantPrinterInt;

public class EnumConstantPrinter implements EnumConstantPrinterInt {

	private final AnnotablePrinterInt AnnotablePrinter;
	private final ArgumentablePrinterInt ArgumentablePrinter;
	private final AnonymousClassPrinterInt AnonymousClassPrinter;

	@Inject
	public EnumConstantPrinter(AnnotablePrinterInt annotablePrinter, ArgumentablePrinterInt argumentablePrinter,
			AnonymousClassPrinterInt anonymousClassPrinter) {
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
