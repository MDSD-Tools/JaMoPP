package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotableAndModifiablePrinterImpl implements Printer<AnnotableAndModifiable> {

	private final Printer<AnnotationInstanceOrModifier> annotationInstanceOrModifierPrinter;

	@Inject
	public AnnotableAndModifiablePrinterImpl(
			final Printer<AnnotationInstanceOrModifier> annotationInstanceOrModifierPrinter) {
		this.annotationInstanceOrModifierPrinter = annotationInstanceOrModifierPrinter;
	}

	@Override
	public void print(final AnnotableAndModifiable element, final BufferedWriter writer) throws IOException {
		for (final AnnotationInstanceOrModifier el : element.getAnnotationsAndModifiers()) {
			annotationInstanceOrModifierPrinter.print(el, writer);
		}
	}

}
