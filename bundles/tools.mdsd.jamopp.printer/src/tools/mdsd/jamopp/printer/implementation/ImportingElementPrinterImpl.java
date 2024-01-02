package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportingElement;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ImportingElementPrinterImpl implements Printer<ImportingElement> {

	private final Printer<Import> importPrinter;

	@Inject
	public ImportingElementPrinterImpl(Printer<Import> importPrinter) {
		this.importPrinter = importPrinter;
	}

	@Override
	public void print(ImportingElement element, BufferedWriter writer) throws IOException {
		for (Import ele : element.getImports()) {
			this.importPrinter.print(ele, writer);
		}
	}

}
