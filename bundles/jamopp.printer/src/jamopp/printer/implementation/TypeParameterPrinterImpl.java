package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class TypeParameterPrinterImpl implements Printer<TypeParameter> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public TypeParameterPrinterImpl(Printer<Annotable> annotablePrinter, Printer<TypeReference> typeReferencePrinter) {
		this.annotablePrinter = annotablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(TypeParameter element, BufferedWriter writer) throws IOException {
		this.annotablePrinter.print(element, writer);
		writer.append(element.getName());
		if (!element.getExtendTypes().isEmpty()) {
			writer.append(" extends ");
			this.typeReferencePrinter.print(element.getExtendTypes().get(0), writer);
			for (var index = 1; index < element.getExtendTypes().size(); index++) {
				writer.append(" & ");
				this.typeReferencePrinter.print(element.getExtendTypes().get(index), writer);
			}
		}
	}

}
