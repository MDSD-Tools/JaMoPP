package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import jamopp.printer.interfaces.Printer;

class AnnotableAndModifiablePrinter implements Printer<AnnotableAndModifiable> {

	private final AnnotationInstanceOrModifierPrinter AnnotationInstanceOrModifierPrinter;
	
	public void print(AnnotableAndModifiable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstanceOrModifier el : element.getAnnotationsAndModifiers()) {
			AnnotationInstanceOrModifierPrinter.print(el, writer);
		}
	}

}
