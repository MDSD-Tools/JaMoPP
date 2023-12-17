package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConcreteClassifierPrinterImpl implements Printer<ConcreteClassifier> {

	private final Printer<Annotation> annotationPrinter;
	private final Printer<org.emftext.language.java.classifiers.Class> classPrinter;
	private final Printer<Enumeration> enumerationPrinter;
	private final Printer<Interface> interfacePrinter;

	@Inject
	public ConcreteClassifierPrinterImpl(Printer<org.emftext.language.java.classifiers.Class> classPrinter,
			Printer<Interface> interfacePrinter, Printer<Enumeration> enumerationPrinter,
			Printer<Annotation> annotationPrinter) {
		this.classPrinter = classPrinter;
		this.interfacePrinter = interfacePrinter;
		this.enumerationPrinter = enumerationPrinter;
		this.annotationPrinter = annotationPrinter;
	}

	@Override
	public void print(ConcreteClassifier element, BufferedWriter writer) throws IOException {
		if (element instanceof org.emftext.language.java.classifiers.Class) {
			this.classPrinter.print((org.emftext.language.java.classifiers.Class) element, writer);
		} else if (element instanceof Interface) {
			this.interfacePrinter.print((Interface) element, writer);
		} else if (element instanceof Enumeration) {
			this.enumerationPrinter.print((Enumeration) element, writer);
		} else {
			this.annotationPrinter.print((Annotation) element, writer);
		}
	}

}
