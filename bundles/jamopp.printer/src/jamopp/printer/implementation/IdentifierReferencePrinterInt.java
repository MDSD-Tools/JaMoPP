package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.IdentifierReference;

public interface IdentifierReferencePrinterInt {

	void print(IdentifierReference element, BufferedWriter writer) throws IOException;

}