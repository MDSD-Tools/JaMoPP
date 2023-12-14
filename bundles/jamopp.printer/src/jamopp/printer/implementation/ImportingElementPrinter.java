package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportingElement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ImportPrinterInt;
import jamopp.printer.interfaces.printer.ImportingElementPrinterInt;

public class ImportingElementPrinter implements ImportingElementPrinterInt {

	private final ImportPrinterInt ImportPrinter;

	@Inject
	public ImportingElementPrinter(ImportPrinterInt importPrinter) {
		ImportPrinter = importPrinter;
	}

	@Override
	public void print(ImportingElement element, BufferedWriter writer) throws IOException {
		for (Import ele : element.getImports()) {
			ImportPrinter.print(ele, writer);
		}
	}

}
