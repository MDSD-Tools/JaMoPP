package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AnnotableAndModifiablePrinter implements AnnotableAndModifiablePrinterInt {

	private final AnnotationInstanceOrModifierPrinter AnnotationInstanceOrModifierPrinter;

	@Inject
	public AnnotableAndModifiablePrinter(
			jamopp.printer.implementation.AnnotationInstanceOrModifierPrinter annotationInstanceOrModifierPrinter) {
		super();
		AnnotationInstanceOrModifierPrinter = annotationInstanceOrModifierPrinter;
	}

	@Override
	public void print(AnnotableAndModifiable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstanceOrModifier el : element.getAnnotationsAndModifiers()) {
			AnnotationInstanceOrModifierPrinter.print(el, writer);
		}
	}

}
