package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.arrays.ArrayInitializer;
import tools.mdsd.jamopp.model.java.expressions.Expression;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotationValuePrinterImpl implements Printer<AnnotationValue> {

	private final Provider<Printer<AnnotationInstance>> annotationInstancePrinter;
	private final Printer<ArrayInitializer> arrayInitializerPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public AnnotationValuePrinterImpl(Provider<Printer<AnnotationInstance>> annotationInstancePrinter,
			Printer<ArrayInitializer> arrayInitializerPrinter, Printer<Expression> expressionPrinter) {
		this.annotationInstancePrinter = annotationInstancePrinter;
		this.arrayInitializerPrinter = arrayInitializerPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(AnnotationValue element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			this.annotationInstancePrinter.get().print((AnnotationInstance) element, writer);
		} else if (element instanceof ArrayInitializer) {
			this.arrayInitializerPrinter.print((ArrayInitializer) element, writer);
		} else {
			this.expressionPrinter.print((Expression) element, writer);
		}
	}

}
