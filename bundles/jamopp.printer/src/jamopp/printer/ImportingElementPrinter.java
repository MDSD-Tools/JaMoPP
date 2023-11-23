package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportingElement;

public class ImportingElementPrinter {

	static void printImportingElement(ImportingElement element, BufferedWriter writer) throws IOException {
		for (Import ele : element.getImports()) {
			ImportPrinter.printImport(ele, writer);
		}
	}

}
