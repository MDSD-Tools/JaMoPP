package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class PrimitiveTypePrinterImpl implements Printer<PrimitiveType> {

	private final Printer<Annotable> annotablePrinter;

	@Inject
	public PrimitiveTypePrinterImpl(Printer<Annotable> annotablePrinter) {
		this.annotablePrinter = annotablePrinter;
	}

	@Override
	public void print(PrimitiveType element, BufferedWriter writer) throws IOException {
		this.annotablePrinter.print(element, writer);
		if (element instanceof tools.mdsd.jamopp.model.java.types.Boolean) {
			writer.append("boolean");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Byte) {
			writer.append("byte");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Char) {
			writer.append("char");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Double) {
			writer.append("double");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Float) {
			writer.append("float");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Int) {
			writer.append("int");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Long) {
			writer.append("long");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Short) {
			writer.append("short");
		} else if (element instanceof tools.mdsd.jamopp.model.java.types.Void) {
			writer.append("void");
		}
	}

}
