package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

import jamopp.printer.interfaces.Printer;

class AnnotationValuePrinter implements Printer<AnnotationValue>{

	public void print(AnnotationValue element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.print((AnnotationInstance) element, writer);
		} else if (element instanceof ArrayInitializer) {
			ArrayInitializerPrinter.print((ArrayInitializer) element, writer);
		} else {
			ExpressionPrinter.print((Expression) element, writer);
		}
	}

}
