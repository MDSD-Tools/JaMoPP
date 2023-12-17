package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.annotations.AnnotationInstance;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotablePrinterImpl implements Printer<Annotable> {

	private final Printer<AnnotationInstance> annotationInstancePrinter;

	@Inject
	public AnnotablePrinterImpl(Printer<AnnotationInstance> annotationInstancePrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
	}

	@Override
	public void print(Annotable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstance inst : element.getAnnotations()) {
			this.annotationInstancePrinter.print(inst, writer);
		}
	}

}
