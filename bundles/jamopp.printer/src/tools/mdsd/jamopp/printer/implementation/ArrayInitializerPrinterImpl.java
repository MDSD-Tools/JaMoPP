package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ArrayInitializerPrinterImpl implements Printer<ArrayInitializer> {

	private final Printer<AnnotationInstance> annotationInstancePrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public ArrayInitializerPrinterImpl(Printer<AnnotationInstance> annotationInstancePrinter,
			Printer<Expression> expressionPrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ArrayInitializer element, BufferedWriter writer) throws IOException {
		writer.append("{");
		for (var index = 0; index < element.getInitialValues().size(); index++) {
			var val = element.getInitialValues().get(index);
			if (val instanceof AnnotationInstance) {
				this.annotationInstancePrinter.print((AnnotationInstance) val, writer);
			} else if (val instanceof ArrayInitializer) {
				print((ArrayInitializer) val, writer);
			} else {
				this.expressionPrinter.print((Expression) val, writer);
			}
			if (index < element.getInitialValues().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append("}");
	}

}
