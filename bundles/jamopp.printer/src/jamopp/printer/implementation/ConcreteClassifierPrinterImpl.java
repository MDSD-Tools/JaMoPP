package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AnnotationPrinterInt;
import jamopp.printer.interfaces.printer.ClassPrinterInt;
import jamopp.printer.interfaces.printer.ConcreteClassifierPrinterInt;
import jamopp.printer.interfaces.printer.EnumerationPrinterInt;
import jamopp.printer.interfaces.printer.InterfacePrinterInt;

public class ConcreteClassifierPrinterImpl implements ConcreteClassifierPrinterInt {

	private final ClassPrinterInt ClassPrinter;
	private final InterfacePrinterInt InterfacePrinter;
	private final EnumerationPrinterInt EnumerationPrinter;
	private final AnnotationPrinterInt AnnotationPrinter;

	@Inject
	public ConcreteClassifierPrinterImpl(ClassPrinterInt classPrinter, InterfacePrinterInt interfacePrinter,
			EnumerationPrinterInt enumerationPrinter, AnnotationPrinterInt annotationPrinter) {
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
