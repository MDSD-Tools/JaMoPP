package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.variables.LocalVariable;
import org.emftext.language.java.variables.Resource;

class ResourcePrinter {

	static void print(Resource element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			LocalVariablePrinter.print((LocalVariable) element, writer);
		} else {
			ElementReferencePrinter.print((ElementReference) element, writer);
		}
	}

}
