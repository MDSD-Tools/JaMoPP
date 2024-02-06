package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotationInstanceOrModifierPrinterImpl implements Printer<AnnotationInstanceOrModifier> {

	private final Printer<AnnotationInstance> annotationInstancePrinter;
	private final Printer<Modifier> modifierPrinter;

	@Inject
	public AnnotationInstanceOrModifierPrinterImpl(final Printer<AnnotationInstance> annotationInstancePrinter,
			final Printer<Modifier> modifierPrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
		this.modifierPrinter = modifierPrinter;
	}

	@Override
	public void print(final AnnotationInstanceOrModifier element, final BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			annotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else {
			modifierPrinter.print((Modifier) element, writer);
		}
	}

}
