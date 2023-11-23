package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.UsesModuleDirective;

public class UsesModuleDirectivePrinter {

	static void printUsesModuleDirective(UsesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("uses ");
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		writer.append(";\n");
	}

}
