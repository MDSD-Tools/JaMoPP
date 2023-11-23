package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ExportsModuleDirective;

class ExportsModuleDirectivePrinter {

	static void print(ExportsModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("exports ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
