package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.containers.JavaRoot;

import jamopp.printer.interfaces.Printer;

public class JavaRootPrinter implements Printer<JavaRoot>{

	private final ImportingElementPrinter ImportingElementPrinter;
	private final ModulePrinter ModulePrinter;
	private final AnnotablePrinter AnnotablePrinter;
	private final CompilationUnitPrinter CompilationUnitPrinter;
	
	/**
	 * Converts a model instance to text and writes it.
	 *
	 * @param root   the model instance to print.
	 * @param writer writer in which the text is written.
	 * @throws IOException if the text cannot be written.
	 */
	public void print(JavaRoot root, BufferedWriter writer) throws IOException {
		if (root instanceof org.emftext.language.java.containers.Module) {
			ImportingElementPrinter.print(root, writer);
			ModulePrinter.print((org.emftext.language.java.containers.Module) root, writer);
		} else {
			if (!root.getNamespaces().isEmpty()) {
				AnnotablePrinter.print(root, writer);
				writer.append("package " + root.getNamespacesAsString() + ";\n\n");
			}
			ImportingElementPrinter.print(root, writer);
			if (root instanceof CompilationUnit) {
				CompilationUnitPrinter.print((CompilationUnit) root, writer);
			}
		}
	}

}
