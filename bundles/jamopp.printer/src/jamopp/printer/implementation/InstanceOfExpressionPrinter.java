package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.InstanceOfExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.InstanceOfExpressionPrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class InstanceOfExpressionPrinter implements InstanceOfExpressionPrinterInt {

	private final InstanceOfExpressionChildPrinterInt InstanceOfExpressionChildPrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;

	@Inject
	public InstanceOfExpressionPrinter(InstanceOfExpressionChildPrinterInt instanceOfExpressionChildPrinter,
			TypeReferencePrinterInt typeReferencePrinter, ArrayDimensionsPrinterInt arrayDimensionsPrinter) {
		InstanceOfExpressionChildPrinter = instanceOfExpressionChildPrinter;
		TypeReferencePrinter = typeReferencePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(InstanceOfExpression element, BufferedWriter writer) throws IOException {
		InstanceOfExpressionChildPrinter.print(element.getChild(), writer);
		writer.append(" instanceof ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
