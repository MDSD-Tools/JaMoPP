package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.annotations.AnnotationInstance;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.AnnotationInstancePrinterInt;

public class AnnotablePrinterImpl implements AnnotablePrinterInt {

	private final AnnotationInstancePrinterInt AnnotationInstancePrinter;

	@Inject
	public AnnotablePrinterImpl(AnnotationInstancePrinterInt annotationInstancePrinter) {
		AnnotationInstancePrinter = annotationInstancePrinter;
	}

	@Override
	public void print(Annotable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstance inst : element.getAnnotations()) {
			AnnotationInstancePrinter.print(inst, writer);
		}
	}

}
