package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.OpensModuleDirective;

class OpensModuleDirectivePrinter {

	static void print(OpensModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("opens ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
