package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.printer.interfaces.Printer;

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
