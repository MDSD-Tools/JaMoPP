package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Interface;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConcreteClassifierPrinterImpl implements Printer<ConcreteClassifier> {

	private final Printer<Annotation> annotationPrinter;
	private final Printer<tools.mdsd.jamopp.model.java.classifiers.Class> classPrinter;
	private final Printer<Enumeration> enumerationPrinter;
	private final Printer<Interface> interfacePrinter;

	@Inject
	public ConcreteClassifierPrinterImpl(Printer<tools.mdsd.jamopp.model.java.classifiers.Class> classPrinter,
			Printer<Interface> interfacePrinter, Printer<Enumeration> enumerationPrinter,
			Printer<Annotation> annotationPrinter) {
		this.classPrinter = classPrinter;
		this.interfacePrinter = interfacePrinter;
		this.enumerationPrinter = enumerationPrinter;
		this.annotationPrinter = annotationPrinter;
	}

	@Override
	public void print(ConcreteClassifier element, BufferedWriter writer) throws IOException {
		if (element instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
			this.classPrinter.print((tools.mdsd.jamopp.model.java.classifiers.Class) element, writer);
		} else if (element instanceof Interface) {
			this.interfacePrinter.print((Interface) element, writer);
		} else if (element instanceof Enumeration) {
			this.enumerationPrinter.print((Enumeration) element, writer);
		} else {
			this.annotationPrinter.print((Annotation) element, writer);
		}
	}

}
