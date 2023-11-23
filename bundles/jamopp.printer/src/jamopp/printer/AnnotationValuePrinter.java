package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

public class AnnotationValuePrinter {

	static void printAnnotationValue(AnnotationValue element, BufferedWriter writer) throws IOException {
		if (element instanceof AnnotationInstance) {
			AnnotationInstancePrinter.printAnnotationInstance((AnnotationInstance) element, writer);
		} else if (element instanceof ArrayInitializer) {
			ArrayInitializerPrinter.printArrayInitializer((ArrayInitializer) element, writer);
		} else {
			ExpressionPrinter.printExpression((Expression) element, writer);
		}
	}

}
