package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.Modifier;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AnnotationInstanceOrModifierPrinterImpl implements Printer<AnnotationInstanceOrModifier> {

	private final Printer<AnnotationInstance> AnnotationInstancePrinter;
	private final Printer<Modifier> ModifierPrinter;

	@Inject
	public AnnotationInstanceOrModifierPrinterImpl(Printer<AnnotationInstance> annotationInstancePrinter,
			Printer<Modifier> modifierPrinter) {
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
