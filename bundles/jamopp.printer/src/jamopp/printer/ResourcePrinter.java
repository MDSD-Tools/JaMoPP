package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.variables.LocalVariable;
import org.emftext.language.java.variables.Resource;

public class ResourcePrinter {

	static void printResource(Resource element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			LocalVariablePrinter.printLocalVariable((LocalVariable) element, writer);
		} else {
			ElementReferencePrinter.printElementReference((ElementReference) element, writer);
		}
	}

}
