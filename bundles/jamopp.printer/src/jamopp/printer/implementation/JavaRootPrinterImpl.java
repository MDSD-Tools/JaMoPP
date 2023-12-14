package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.containers.JavaRoot;
import org.emftext.language.java.imports.ImportingElement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class JavaRootPrinterImpl implements Printer<JavaRoot> {

	private final Printer<Annotable> AnnotablePrinter;
	private final Printer<CompilationUnit> CompilationUnitPrinter;
	private final Printer<ImportingElement> ImportingElementPrinter;
	private final Printer<org.emftext.language.java.containers.Module> ModulePrinter;

	@Inject
	public JavaRootPrinterImpl(Printer<ImportingElement> importingElementPrinter, Printer<org.emftext.language.java.containers.Module> modulePrinter,
			Printer<Annotable> annotablePrinter, Printer<CompilationUnit> compilationUnitPrinter) {
		ImportingElementPrinter = importingElementPrinter;
		ModulePrinter = modulePrinter;
		AnnotablePrinter = annotablePrinter;
		CompilationUnitPrinter = compilationUnitPrinter;
	}

	/**
	 * Converts a model instance to text and writes it.
	 *
	 * @param root   the model instance to print.
	 * @param writer writer in which the text is written.
	 * @throws IOException if the text cannot be written.
	 */
	@Override
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
