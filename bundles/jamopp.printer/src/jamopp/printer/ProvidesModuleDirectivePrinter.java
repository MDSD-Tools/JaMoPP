package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ProvidesModuleDirective;
import org.emftext.language.java.types.TypeReference;

public class ProvidesModuleDirectivePrinter {

	static void printProvidesModuleDirective(ProvidesModuleDirective element, BufferedWriter writer)
			throws IOException {
		writer.append("provides ");
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		writer.append(" with ");
		for (int index = 0; index < element.getServiceProviders().size(); index++) {
			TypeReference ref = element.getServiceProviders().get(index);
			TypeReferencePrinter.printTypeReference(ref, writer);
			if (index < element.getServiceProviders().size() - 1) {
				writer.append(".");
			}
		}
		writer.append(";\n");
	}

}
