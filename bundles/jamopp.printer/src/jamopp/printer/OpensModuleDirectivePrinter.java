package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.OpensModuleDirective;

public class OpensModuleDirectivePrinter {

	static void printOpensModuleDirective(OpensModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("opens ");
		RemainingAccessProvidingModuleDirectivePrinter.printRemainingAccessProvidingModuleDirective(element, writer);
	}

}
