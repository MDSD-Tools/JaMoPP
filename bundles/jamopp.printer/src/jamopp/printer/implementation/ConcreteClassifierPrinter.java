package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConcreteClassifierPrinterInt;

class ConcreteClassifierPrinter implements Printer<ConcreteClassifier>, ConcreteClassifierPrinterInt {

	private final ClassPrinter ClassPrinter;
	private final InterfacePrinter InterfacePrinter;
	private final EnumerationPrinter EnumerationPrinter;
	private final AnnotationPrinter AnnotationPrinter;

	@Inject
	public ConcreteClassifierPrinter(jamopp.printer.implementation.ClassPrinter classPrinter,
			jamopp.printer.implementation.InterfacePrinter interfacePrinter,
			jamopp.printer.implementation.EnumerationPrinter enumerationPrinter,
			jamopp.printer.implementation.AnnotationPrinter annotationPrinter) {
		super();
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
