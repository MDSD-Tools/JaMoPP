package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpression;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpressionChild;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class InstanceOfExpressionPrinterImpl implements Printer<InstanceOfExpression> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public InstanceOfExpressionPrinterImpl(final Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter,
			final Printer<TypeReference> typeReferencePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.instanceOfExpressionChildPrinter = instanceOfExpressionChildPrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(final InstanceOfExpression element, final BufferedWriter writer) throws IOException {
		instanceOfExpressionChildPrinter.print(element.getChild(), writer);
		writer.append(" instanceof ");
		typeReferencePrinter.print(element.getTypeReference(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
