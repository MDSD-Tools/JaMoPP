package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.containers.JavaRoot;
import org.emftext.language.java.containers.Module;

public class JavaRootPrinter {

	/**
	 * Converts a model instance to text and writes it.
	 *
	 * @param root   the model instance to print.
	 * @param writer writer in which the text is written.
	 * @throws IOException if the text cannot be written.
	 */
	static void printJavaRoot(JavaRoot root, BufferedWriter writer) throws IOException {
		if (root instanceof org.emftext.language.java.containers.Module) {
			ImportingElementPrinter.printImportingElement(root, writer);
			ModulePrinter.printModule((org.emftext.language.java.containers.Module) root, writer);
		} else {
			if (!root.getNamespaces().isEmpty()) {
				AnnotablePrinter.printAnnotable(root, writer);
				writer.append("package " + root.getNamespacesAsString() + ";\n\n");
			}
			ImportingElementPrinter.printImportingElement(root, writer);
			if (root instanceof CompilationUnit) {
				CompilationUnitPrinter.printCompilationUnit((CompilationUnit) root, writer);
			}
		}
	}

}
