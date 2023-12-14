package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AnnotableAndModifiablePrinterImpl implements Printer<AnnotableAndModifiable> {

	private final Printer<AnnotationInstanceOrModifier> AnnotationInstanceOrModifierPrinter;

	@Inject
	public AnnotableAndModifiablePrinterImpl(Printer<AnnotationInstanceOrModifier> annotationInstanceOrModifierPrinter) {
		AnnotationInstanceOrModifierPrinter = annotationInstanceOrModifierPrinter;
	}

	@Override
	public void print(AnnotableAndModifiable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstanceOrModifier el : element.getAnnotationsAndModifiers()) {
			AnnotationInstanceOrModifierPrinter.print(el, writer);
		}
	}

}

