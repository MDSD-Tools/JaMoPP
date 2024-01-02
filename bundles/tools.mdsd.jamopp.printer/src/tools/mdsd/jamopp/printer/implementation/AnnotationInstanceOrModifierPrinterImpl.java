package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotationInstanceOrModifierPrinterImpl implements Printer<AnnotationInstanceOrModifier> {

	private final Printer<AnnotationInstance> annotationInstancePrinter;
	private final Printer<Modifier> modifierPrinter;

	@Inject
	public AnnotationInstanceOrModifierPrinterImpl(Printer<AnnotationInstance> annotationInstancePrinter,
			Printer<Modifier> modifierPrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
		this.modifierPrinter = modifierPrinter;
	}

	@Override
	public void print(AnnotationInstanceOrModifier element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			this.annotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else {
			this.modifierPrinter.print((Modifier) element, writer);
		}
	}

}
