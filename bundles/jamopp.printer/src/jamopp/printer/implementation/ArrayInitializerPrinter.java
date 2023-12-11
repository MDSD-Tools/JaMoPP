package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.arrays.ArrayInitializationValue;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.expressions.Expression;

import jamopp.printer.interfaces.Printer;

class ArrayInitializerPrinter implements Printer<ArrayInitializer>{

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