package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArrayInitializationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

public class ArrayInitializerPrinter {

	static void printArrayInitializer(ArrayInitializer element, BufferedWriter writer) throws IOException {
		writer.append("{");
		for (int index = 0; index < element.getInitialValues().size(); index++) {
			ArrayInitializationValue val = element.getInitialValues().get(index);
			if (val instanceof AnnotationInstance) {
				AnnotationInstancePrinter.printAnnotationInstance((AnnotationInstance) val, writer);
			} else if (val instanceof ArrayInitializer) {
				printArrayInitializer((ArrayInitializer) val, writer);
			} else {
				ExpressionPrinter.printExpression((Expression) val, writer);
			}
			if (index < element.getInitialValues().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append("}");
	}

}