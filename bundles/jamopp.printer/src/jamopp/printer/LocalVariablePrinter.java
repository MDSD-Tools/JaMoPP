package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.variables.AdditionalLocalVariable;
import org.emftext.language.java.variables.LocalVariable;

public class LocalVariablePrinter {

	static void printLocalVariable(LocalVariable element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.printAnnotableAndModifiable(element, writer);
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		TypeArgumentablePrinter.printTypeArgumentable(element, writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			ExpressionPrinter.printExpression(element.getInitialValue(), writer);
		}
		for (AdditionalLocalVariable var : element.getAdditionalLocalVariables()) {
			writer.append(", ");
			AdditionalLocalVariablePrinter.printAdditionalLocalVariable(var, writer);
		}
	}

}
