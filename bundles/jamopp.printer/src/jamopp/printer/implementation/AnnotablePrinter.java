package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.annotations.AnnotationInstance;

import jamopp.printer.interfaces.Printer;

class AnnotablePrinter implements Printer<Annotable>{

	private final AnnotationInstancePrinter AnnotationInstancePrinter;
	
	public void print(Annotable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstance inst : element.getAnnotations()) {
			AnnotationInstancePrinter.print(inst, writer);
		}
	}

}
