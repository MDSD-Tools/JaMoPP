package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.arrays.ArrayInitializer;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ArrayInitializerPrinterImpl implements Printer<ArrayInitializer> {

	private final Printer<AnnotationInstance> annotationInstancePrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public ArrayInitializerPrinterImpl(final Printer<AnnotationInstance> annotationInstancePrinter,
			final Printer<Expression> expressionPrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final ArrayInitializer element, final BufferedWriter writer) throws IOException {
		writer.append("{");
		for (var index = 0; index < element.getInitialValues().size(); index++) {
			final var val = element.getInitialValues().get(index);
			if (val instanceof AnnotationInstance) {
				annotationInstancePrinter.print((AnnotationInstance) val, writer);
			} else if (val instanceof ArrayInitializer) {
				print((ArrayInitializer) val, writer);
			} else {
				expressionPrinter.print((Expression) val, writer);
			}
			if (index < element.getInitialValues().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append("}");
	}

}
