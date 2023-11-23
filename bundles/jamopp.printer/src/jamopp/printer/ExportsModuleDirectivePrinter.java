package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ExportsModuleDirective;

public class ExportsModuleDirectivePrinter {

	static void printExportsModuleDirective(ExportsModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("exports ");
		RemainingAccessProvidingModuleDirectivePrinter.printRemainingAccessProvidingModuleDirective(element, writer);
	}

}
