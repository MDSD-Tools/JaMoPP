package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.variables.LocalVariable;
import org.emftext.language.java.variables.Resource;

import jamopp.printer.interfaces.Printer;

class ResourcePrinter implements Printer<Resource>{

	private final LocalVariablePrinter LocalVariablePrinter;
	private final ElementReferencePrinter ElementReferencePrinter;
	
	public void print(Resource element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			LocalVariablePrinter.print((LocalVariable) element, writer);
		} else {
			ElementReferencePrinter.print((ElementReference) element, writer);
		}
	}

}
