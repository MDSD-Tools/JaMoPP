package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.InstanceOfExpression;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InstanceOfExpressionPrinterImpl implements Printer<InstanceOfExpression> {

	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<InstanceOfExpressionChild> InstanceOfExpressionChildPrinter;
	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public InstanceOfExpressionPrinterImpl(Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
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
