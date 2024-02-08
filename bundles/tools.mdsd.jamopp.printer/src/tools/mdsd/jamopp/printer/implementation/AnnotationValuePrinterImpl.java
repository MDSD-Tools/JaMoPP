package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;
import javax.inject.Provider;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.arrays.ArrayInitializer;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotationValuePrinterImpl implements Printer<AnnotationValue> {

	private final Provider<Printer<AnnotationInstance>> annotationInstancePrinter;
	private final Printer<ArrayInitializer> arrayInitializerPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public AnnotationValuePrinterImpl(final Provider<Printer<AnnotationInstance>> annotationInstancePrinter,
			final Printer<ArrayInitializer> arrayInitializerPrinter, final Printer<Expression> expressionPrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
		this.arrayInitializerPrinter = arrayInitializerPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final AnnotationValue element, final BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			annotationInstancePrinter.get().print((AnnotationInstance) element, writer);
		} else if (element instanceof ArrayInitializer) {
			arrayInitializerPrinter.print((ArrayInitializer) element, writer);
		} else {
			expressionPrinter.print((Expression) element, writer);
		}
	}

}
