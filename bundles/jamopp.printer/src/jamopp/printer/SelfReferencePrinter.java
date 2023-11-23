package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.SelfReference;

class SelfReferencePrinter {

	static void print(SelfReference element, BufferedWriter writer) throws IOException {
		SelfPrinter.print(element.getSelf(), writer);
	}

}
