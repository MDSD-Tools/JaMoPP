package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.imports.ImportingElement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class JavaRootPrinterImpl implements Printer<JavaRoot> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<CompilationUnit> compilationUnitPrinter;
	private final Printer<ImportingElement> importingElementPrinter;
	private final Printer<tools.mdsd.jamopp.model.java.containers.Module> modulePrinter;

	@Inject
	public JavaRootPrinterImpl(Printer<ImportingElement> importingElementPrinter,
			Printer<tools.mdsd.jamopp.model.java.containers.Module> modulePrinter, Printer<Annotable> annotablePrinter,
			Printer<CompilationUnit> compilationUnitPrinter) {
		this.importingElementPrinter = importingElementPrinter;
		this.modulePrinter = modulePrinter;
		this.annotablePrinter = annotablePrinter;
		this.compilationUnitPrinter = compilationUnitPrinter;
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
		if (root instanceof tools.mdsd.jamopp.model.java.containers.Module) {
			this.importingElementPrinter.print(root, writer);
			this.modulePrinter.print((tools.mdsd.jamopp.model.java.containers.Module) root, writer);
		} else {
			if (!root.getNamespaces().isEmpty()) {
				this.annotablePrinter.print(root, writer);
				writer.append("package " + root.getNamespacesAsString() + ";\n\n");
			}
			this.importingElementPrinter.print(root, writer);
			if (root instanceof CompilationUnit) {
				this.compilationUnitPrinter.print((CompilationUnit) root, writer);
			}
		}
	}

}
