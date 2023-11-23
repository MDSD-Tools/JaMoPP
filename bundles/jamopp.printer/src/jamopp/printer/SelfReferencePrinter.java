package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.SelfReference;

public class SelfReferencePrinter {

	static void printSelfReference(SelfReference element, BufferedWriter writer) throws IOException {
		SelfPrinter.printSelf(element.getSelf(), writer);
	}

}
