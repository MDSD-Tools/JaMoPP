package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotationValuePrinterInt;

public class AnnotationValuePrinter implements AnnotationValuePrinterInt {

	private final AnnotationInstancePrinter AnnotationInstancePrinter;
	private final ArrayInitializerPrinter ArrayInitializerPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public AnnotationValuePrinter(jamopp.printer.implementation.AnnotationInstancePrinter annotationInstancePrinter,
			jamopp.printer.implementation.ArrayInitializerPrinter arrayInitializerPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
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
