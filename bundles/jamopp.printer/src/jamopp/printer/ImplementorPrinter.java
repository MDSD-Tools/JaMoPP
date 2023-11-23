package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Implementor;

public class ImplementorPrinter {

	static void printImplementor(Implementor element, BufferedWriter writer) throws IOException {
		if (!element.getImplements().isEmpty()) {
			writer.append("implements ");
			TypeReferencePrinter.printTypeReference(element.getImplements().get(0), writer);
			for (int index = 1; index < element.getImplements().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.printTypeReference(element.getImplements().get(index), writer);
			}
			writer.append(" ");
		}
	}

}
