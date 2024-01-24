package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportingElement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ImportingElementPrinterImpl implements Printer<ImportingElement> {

	private final Printer<Import> importPrinter;

	@Inject
	public ImportingElementPrinterImpl(final Printer<Import> importPrinter) {
		this.importPrinter = importPrinter;
	}

	@Override
	public void print(final ImportingElement element, final BufferedWriter writer) throws IOException {
		for (final Import ele : element.getImports()) {
			importPrinter.print(ele, writer);
		}
	}

}
