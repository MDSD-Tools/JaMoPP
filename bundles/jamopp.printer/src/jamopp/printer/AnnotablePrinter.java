package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.annotations.AnnotationInstance;

public class AnnotablePrinter {

	static void printAnnotable(Annotable element, BufferedWriter writer) throws IOException {
		for (AnnotationInstance inst : element.getAnnotations()) {
			AnnotationInstancePrinter.printAnnotationInstance(inst, writer);
		}
	}

}
