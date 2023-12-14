package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.AnnotationInstanceOrModifierPrinterInt;

public class AnnotableAndModifiablePrinterImpl implements AnnotableAndModifiablePrinterInt {

	private final AnnotationInstanceOrModifierPrinterInt AnnotationInstanceOrModifierPrinter;

	@Inject
	public AnnotableAndModifiablePrinterImpl(AnnotationInstanceOrModifierPrinterInt annotationInstanceOrModifierPrinter) {
		AnnotationInstanceOrModifierPrinter = annotationInstanceOrModifierPrinter;
	}

	@Override
	public void print(AnnotableAndModifiable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstanceOrModifier el : element.getAnnotationsAndModifiers()) {
			AnnotationInstanceOrModifierPrinter.print(el, writer);
		}
	}

}
