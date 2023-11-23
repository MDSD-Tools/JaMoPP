package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Implementor;

class ImplementorPrinter {

	static void print(Implementor element, BufferedWriter writer) throws IOException {
		if (!element.getImplements().isEmpty()) {
			writer.append("implements ");
			TypeReferencePrinter.print(element.getImplements().get(0), writer);
			for (int index = 1; index < element.getImplements().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.print(element.getImplements().get(index), writer);
			}
			writer.append(" ");
		}
	}

}
