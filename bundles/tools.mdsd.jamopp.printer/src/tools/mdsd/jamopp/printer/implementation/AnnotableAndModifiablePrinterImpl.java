package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotableAndModifiablePrinterImpl implements Printer<AnnotableAndModifiable> {

	private final Printer<AnnotationInstanceOrModifier> annotationInstanceOrModifierPrinter;

	@Inject
	public AnnotableAndModifiablePrinterImpl(
			Printer<AnnotationInstanceOrModifier> annotationInstanceOrModifierPrinter) {
		this.annotationInstanceOrModifierPrinter = annotationInstanceOrModifierPrinter;
	}

	@Override
	public void print(AnnotableAndModifiable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstanceOrModifier el : element.getAnnotationsAndModifiers()) {
			this.annotationInstanceOrModifierPrinter.print(el, writer);
		}
	}

}
