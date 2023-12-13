package jamopp.printer.interfaces.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.IdentifierReference;

public interface IdentifierReferencePrinterInt {

	void print(IdentifierReference element, BufferedWriter writer) throws IOException;

}