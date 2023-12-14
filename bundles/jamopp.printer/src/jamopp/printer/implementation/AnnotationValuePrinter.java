package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotationInstancePrinterInt;
import jamopp.printer.interfaces.printer.AnnotationValuePrinterInt;
import jamopp.printer.interfaces.printer.ArrayInitializerPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;

public class AnnotationValuePrinter implements AnnotationValuePrinterInt {

	private final AnnotationInstancePrinterInt AnnotationInstancePrinter;
	private final ArrayInitializerPrinterInt ArrayInitializerPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public AnnotationValuePrinter(AnnotationInstancePrinterInt annotationInstancePrinter,
			ArrayInitializerPrinterInt arrayInitializerPrinter, ExpressionPrinterInt expressionPrinter) {
		AnnotationInstancePrinter = annotationInstancePrinter;
		ArrayInitializerPrinter = arrayInitializerPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
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
