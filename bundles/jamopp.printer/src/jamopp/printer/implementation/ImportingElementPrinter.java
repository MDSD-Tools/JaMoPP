package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportingElement;

import jamopp.printer.interfaces.Printer;

class ImportingElementPrinter implements Printer<ImportingElement> {

	public void print(ImportingElement element, BufferedWriter writer) throws IOException {
		for (Import ele : element.getImports()) {
			ImportPrinter.print(ele, writer);
		}
	}

}
