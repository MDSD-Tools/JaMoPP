package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.modifiers.Modifier;

public class AnnotationInstanceOrModifierPrinter {

	static void printAnnotationInstanceOrModifier(AnnotationInstanceOrModifier element, BufferedWriter writer)
			throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.printAnnotationInstance((AnnotationInstance) element, writer);
		} else {
			ModifierPrinter.printModifier((Modifier) element, writer);
		}
	}

}
