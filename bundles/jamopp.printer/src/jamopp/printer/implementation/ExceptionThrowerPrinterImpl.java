package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.ExceptionThrower;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class ExceptionThrowerPrinterImpl implements Printer<ExceptionThrower> {

	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public ExceptionThrowerPrinterImpl(TypeReferencePrinterInt typeReferencePrinter) {
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(ExceptionThrower element, BufferedWriter writer) throws IOException {
		if (!element.getExceptions().isEmpty()) {
			writer.append("throws ");
			TypeReferencePrinter.print(element.getExceptions().get(0), writer);
			for (var index = 1; index < element.getExceptions().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.print(element.getExceptions().get(index), writer);
			}
		}
	}



}
