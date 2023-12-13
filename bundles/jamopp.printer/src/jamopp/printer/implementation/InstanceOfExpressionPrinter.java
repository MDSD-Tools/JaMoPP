package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class InstanceOfExpressionPrinter implements Printer<InstanceOfExpression> {

	private final InstanceOfExpressionChildPrinter InstanceOfExpressionChildPrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;

	@Inject
	public InstanceOfExpressionPrinter(
			jamopp.printer.implementation.InstanceOfExpressionChildPrinter instanceOfExpressionChildPrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter) {
		super();
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
