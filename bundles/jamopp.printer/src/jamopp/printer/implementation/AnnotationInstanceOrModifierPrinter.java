package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.Modifier;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AnnotationInstanceOrModifierPrinter implements Printer<AnnotationInstanceOrModifier> {

	private final AnnotationInstancePrinter AnnotationInstancePrinter;
	private final ModifierPrinter ModifierPrinter;

	@Inject
	public AnnotationInstanceOrModifierPrinter(
			jamopp.printer.implementation.AnnotationInstancePrinter annotationInstancePrinter,
			jamopp.printer.implementation.ModifierPrinter modifierPrinter) {
		super();
		AnnotationInstancePrinter = annotationInstancePrinter;
		ModifierPrinter = modifierPrinter;
	}

	public void print(AnnotationInstanceOrModifier element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else {
			ModifierPrinter.print((Modifier) element, writer);
		}
	}

}
