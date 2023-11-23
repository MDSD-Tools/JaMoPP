package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.ExceptionThrower;

class ExceptionThrowerPrinter {

	static void print(ExceptionThrower element, BufferedWriter writer) throws IOException {
		if (!element.getExceptions().isEmpty()) {
			writer.append("throws ");
			TypeReferencePrinter.print(element.getExceptions().get(0), writer);
			for (int index = 1; index < element.getExceptions().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.print(element.getExceptions().get(index), writer);
			}
		}
	}

}
