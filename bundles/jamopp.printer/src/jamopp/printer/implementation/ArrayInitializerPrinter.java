package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArrayInitializationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ArrayInitializerPrinterInt;

class ArrayInitializerPrinter implements Printer<ArrayInitializer>, ArrayInitializerPrinterInt {

	private final AnnotationInstancePrinter AnnotationInstancePrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ArrayInitializerPrinter(jamopp.printer.implementation.AnnotationInstancePrinter annotationInstancePrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		AnnotationInstancePrinter = annotationInstancePrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ArrayInitializer element, BufferedWriter writer) throws IOException {
		writer.append("{");
		for (int index = 0; index < element.getInitialValues().size(); index++) {
			ArrayInitializationValue val = element.getInitialValues().get(index);
			if (val instanceof AnnotationInstance) {
				AnnotationInstancePrinter.print((AnnotationInstance) val, writer);
			} else if (val instanceof ArrayInitializer) {
				print((ArrayInitializer) val, writer);
			} else {
				ExpressionPrinter.print((Expression) val, writer);
			}
			if (index < element.getInitialValues().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append("}");
	}

}
