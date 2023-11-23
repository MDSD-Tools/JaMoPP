package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.ExceptionThrower;

public class ExceptionThrowerPrinter {

	static void printExceptionThrower(ExceptionThrower element, BufferedWriter writer) throws IOException {
		if (!element.getExceptions().isEmpty()) {
			writer.append("throws ");
			TypeReferencePrinter.printTypeReference(element.getExceptions().get(0), writer);
			for (int index = 1; index < element.getExceptions().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.printTypeReference(element.getExceptions().get(index), writer);
			}
		}
	}

}
