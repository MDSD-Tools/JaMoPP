package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.EnumConstant;

class EnumConstantPrinter {

	static void print(EnumConstant element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append(element.getName() + " ");
		if (!element.getArguments().isEmpty()) {
			ArgumentablePrinter.print(element, writer);
		}
		if (element.getAnonymousClass() != null) {
			AnonymousClassPrinter.print(element.getAnonymousClass(), writer);
		}
	}

}
