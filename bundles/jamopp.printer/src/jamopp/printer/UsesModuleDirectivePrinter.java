package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.UsesModuleDirective;

class UsesModuleDirectivePrinter {

	static void print(UsesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("uses ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(";\n");
	}

}
