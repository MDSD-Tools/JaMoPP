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

	private final Provider<Printer<AnnotationInstance>> AnnotationInstancePrinter;
	private final Printer<ArrayInitializer> ArrayInitializerPrinter;
	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public AnnotationValuePrinterImpl(Provider<Printer<AnnotationInstance>> annotationInstancePrinter,
			Printer<ArrayInitializer> arrayInitializerPrinter, Printer<Expression> expressionPrinter) {
		AnnotationInstancePrinter = annotationInstancePrinter;
		ArrayInitializerPrinter = arrayInitializerPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(AnnotationValue element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.get().print((AnnotationInstance) element, writer);
		} else if (element instanceof ArrayInitializer) {
			ArrayInitializerPrinter.print((ArrayInitializer) element, writer);
		} else {
			ExpressionPrinter.print((Expression) element, writer);
		}
	}

}
