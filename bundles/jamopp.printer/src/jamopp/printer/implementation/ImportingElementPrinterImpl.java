package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportingElement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ImportingElementPrinterImpl implements Printer<ImportingElement> {

	private final Printer<Import> ImportPrinter;

	@Inject
	public ImportingElementPrinterImpl(Printer<Import> importPrinter) {
		ImportPrinter = importPrinter;
	}

	@Override
	public void print(ImportingElement element, BufferedWriter writer) throws IOException {
		for (Import ele : element.getImports()) {
			ImportPrinter.print(ele, writer);
		}
	}

}
