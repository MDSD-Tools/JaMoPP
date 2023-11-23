package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.Modifier;

class AnnotationInstanceOrModifierPrinter {

	static void print(AnnotationInstanceOrModifier element, BufferedWriter writer)
			throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else {
			ModifierPrinter.print((Modifier) element, writer);
		}
	}

}
