package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportingElement;

class ImportingElementPrinter {

	static void print(ImportingElement element, BufferedWriter writer) throws IOException {
		for (Import ele : element.getImports()) {
			ImportPrinter.print(ele, writer);
		}
	}

}
