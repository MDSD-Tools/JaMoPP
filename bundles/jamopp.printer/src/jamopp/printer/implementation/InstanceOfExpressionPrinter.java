package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpression;

import jamopp.printer.interfaces.Printer;

class InstanceOfExpressionPrinter implements Printer<InstanceOfExpression>{

	public void print(InstanceOfExpression element, BufferedWriter writer)
			throws IOException {
		InstanceOfExpressionChildPrinter.print(element.getChild(), writer);
		writer.append(" instanceof ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
