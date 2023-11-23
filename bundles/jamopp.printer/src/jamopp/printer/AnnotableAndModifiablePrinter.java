package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

public class AnnotableAndModifiablePrinter {

	static void printAnnotableAndModifiable(AnnotableAndModifiable element, BufferedWriter writer)
			throws IOException {
		for (AnnotationInstanceOrModifier el : element.getAnnotationsAndModifiers()) {
			AnnotationInstanceOrModifierPrinter.printAnnotationInstanceOrModifier(el, writer);
		}
	}

}
