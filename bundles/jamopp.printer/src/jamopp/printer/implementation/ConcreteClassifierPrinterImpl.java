package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ConcreteClassifierPrinterImpl implements Printer<ConcreteClassifier> {

	private final Printer<Annotation> AnnotationPrinter;
	private final Printer<org.emftext.language.java.classifiers.Class> ClassPrinter;
	private final Printer<Enumeration> EnumerationPrinter;
	private final Printer<Interface> InterfacePrinter;

	@Inject
	public ConcreteClassifierPrinterImpl(Printer<org.emftext.language.java.classifiers.Class> classPrinter, Printer<Interface> interfacePrinter,
			Printer<Enumeration> enumerationPrinter, Printer<Annotation> annotationPrinter) {
		ClassPrinter = classPrinter;
		InterfacePrinter = interfacePrinter;
		EnumerationPrinter = enumerationPrinter;
		AnnotationPrinter = annotationPrinter;
	}

	@Override
	public void print(ConcreteClassifier element, BufferedWriter writer) throws IOException {
		if (element instanceof org.emftext.language.java.classifiers.Class) {
			ClassPrinter.print((org.emftext.language.java.classifiers.Class) element, writer);
		} else if (element instanceof Interface) {
			InterfacePrinter.print((Interface) element, writer);
		} else if (element instanceof Enumeration) {
			EnumerationPrinter.print((Enumeration) element, writer);
		} else {
			AnnotationPrinter.print((Annotation) element, writer);
		}
	}

}
