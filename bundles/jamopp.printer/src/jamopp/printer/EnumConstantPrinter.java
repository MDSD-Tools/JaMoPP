package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.EnumConstant;

public class EnumConstantPrinter {

	static void printEnumConstant(EnumConstant element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.printAnnotable(element, writer);
		writer.append(element.getName() + " ");
		if (!element.getArguments().isEmpty()) {
			ArgumentablePrinter.printArgumentable(element, writer);
		}
		if (element.getAnonymousClass() != null) {
			AnonymousClassPrinter.printAnonymousClass(element.getAnonymousClass(), writer);
		}
	}

}
