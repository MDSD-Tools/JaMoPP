package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.CompilationUnitPrinterInt;
import jamopp.printer.interfaces.printer.ConcreteClassifierPrinterInt;

public class CompilationUnitPrinterImpl implements Printer<CompilationUnit> {

	private final Printer<ConcreteClassifier> ConcreteClassifierPrinter;

	@Inject
	public CompilationUnitPrinterImpl(Printer<ConcreteClassifier> concreteClassifierPrinter) {
		ConcreteClassifierPrinter = concreteClassifierPrinter;
	}

	@Override
	public void print(CompilationUnit element, BufferedWriter writer) throws IOException {
		for (ConcreteClassifier classifier : element.getClassifiers()) {
			ConcreteClassifierPrinter.print(classifier, writer);
		}
	}

}
