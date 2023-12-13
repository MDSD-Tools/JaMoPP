package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.ExceptionThrower;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExceptionThrowerPrinterInt;

public class ExceptionThrowerPrinter implements ExceptionThrowerPrinterInt {

	private final TypeReferencePrinter TypeReferencePrinter;

	@Inject
	public ExceptionThrowerPrinter(jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter) {
		super();
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(ExceptionThrower element, BufferedWriter writer) throws IOException {
		if (!element.getExceptions().isEmpty()) {
			writer.append("throws ");
			TypeReferencePrinter.print(element.getExceptions().get(0), writer);
			for (int index = 1; index < element.getExceptions().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.print(element.getExceptions().get(index), writer);
			}
		}
	}

}
