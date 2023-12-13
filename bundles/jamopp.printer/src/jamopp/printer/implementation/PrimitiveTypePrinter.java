package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.PrimitiveType;
import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.PrimitiveTypePrinterInt;

public class PrimitiveTypePrinter implements PrimitiveTypePrinterInt {

	private final AnnotablePrinter AnnotablePrinter;

	@Inject
	public PrimitiveTypePrinter(AnnotablePrinter annotablePrinter) {
		super();
		AnnotablePrinter = annotablePrinter;
	}

	@Override
	public void print(PrimitiveType element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		if (element instanceof org.emftext.language.java.types.Boolean) {
			writer.append("boolean");
		} else if (element instanceof org.emftext.language.java.types.Byte) {
			writer.append("byte");
		} else if (element instanceof org.emftext.language.java.types.Char) {
			writer.append("char");
		} else if (element instanceof org.emftext.language.java.types.Double) {
			writer.append("double");
		} else if (element instanceof org.emftext.language.java.types.Float) {
			writer.append("float");
		} else if (element instanceof org.emftext.language.java.types.Int) {
			writer.append("int");
		} else if (element instanceof org.emftext.language.java.types.Long) {
			writer.append("long");
		} else if (element instanceof org.emftext.language.java.types.Short) {
			writer.append("short");
		} else if (element instanceof org.emftext.language.java.types.Void) {
			writer.append("void");
		}
	}

}
