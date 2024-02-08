package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotablePrinterImpl implements Printer<Annotable> {

	private final Printer<AnnotationInstance> annotationInstancePrinter;

	@Inject
	public AnnotablePrinterImpl(final Printer<AnnotationInstance> annotationInstancePrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
	}

	@Override
	public void print(final Annotable element, final BufferedWriter writer) throws IOException {
		for (final AnnotationInstance inst : element.getAnnotations()) {
			annotationInstancePrinter.print(inst, writer);
		}
	}

}
