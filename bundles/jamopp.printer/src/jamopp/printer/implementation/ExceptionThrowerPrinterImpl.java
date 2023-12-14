package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.ExceptionThrower;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ExceptionThrowerPrinterImpl implements Printer<ExceptionThrower> {

	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ExceptionThrowerPrinterImpl(Printer<TypeReference> typeReferencePrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(ExceptionThrower element, BufferedWriter writer) throws IOException {
		if (!element.getExceptions().isEmpty()) {
			writer.append("throws ");
			this.typeReferencePrinter.print(element.getExceptions().get(0), writer);
			for (var index = 1; index < element.getExceptions().size(); index++) {
				writer.append(", ");
				this.typeReferencePrinter.print(element.getExceptions().get(index), writer);
			}
		}
	}

}
