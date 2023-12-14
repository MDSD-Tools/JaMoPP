package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.Modifier;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AnnotationInstanceOrModifierPrinterInt;
import jamopp.printer.interfaces.printer.AnnotationInstancePrinterInt;
import jamopp.printer.interfaces.printer.ModifierPrinterInt;

public class AnnotationInstanceOrModifierPrinterImpl implements AnnotationInstanceOrModifierPrinterInt {

	private final AnnotationInstancePrinterInt AnnotationInstancePrinter;
	private final ModifierPrinterInt ModifierPrinter;

	@Inject
	public AnnotationInstanceOrModifierPrinterImpl(AnnotationInstancePrinterInt annotationInstancePrinter,
			ModifierPrinterInt modifierPrinter) {
		AnnotationInstancePrinter = annotationInstancePrinter;
		ModifierPrinter = modifierPrinter;
	}

	@Override
	public void print(AnnotationInstanceOrModifier element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else {
			ModifierPrinter.print((Modifier) element, writer);
		}
	}

}
