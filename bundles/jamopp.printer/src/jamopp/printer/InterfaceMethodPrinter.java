package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.InterfaceMethod;

public class InterfaceMethodPrinter {

	static void printInterfaceMethod(InterfaceMethod element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.printAnnotableAndModifiable(element, writer);
		TypeParametrizablePrinter.printTypeParametrizable(element, writer);
		writer.append(" ");
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.printParametrizable(element, writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
		ExceptionThrowerPrinter.printExceptionThrower(element, writer);
		writer.append(" ");
		if (element.getDefaultValue() != null) {
			writer.append("default ");
			AnnotationValuePrinter.printAnnotationValue(element.getDefaultValue(), writer);
		}
		StatementPrinter.printStatement(element.getStatement(), writer);
		writer.append("\n");
	}

}
