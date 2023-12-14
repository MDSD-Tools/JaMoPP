package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Field;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AdditionalFieldPrinterInt;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.FieldPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class FieldPrinterImpl implements FieldPrinterInt {

	private final AnnotableAndModifiablePrinterInt AnnotableAndModifiablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;
	private final AdditionalFieldPrinterInt AdditionalFieldPrinter;

	@Inject
	public FieldPrinterImpl(AnnotableAndModifiablePrinterInt annotableAndModifiablePrinter,
			TypeReferencePrinterInt typeReferencePrinter, TypeArgumentablePrinterInt typeArgumentablePrinter,
			ArrayDimensionsPrinterInt arrayDimensionsPrinter, ExpressionPrinterInt expressionPrinter,
			AdditionalFieldPrinterInt additionalFieldPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ExpressionPrinter = expressionPrinter;
		AdditionalFieldPrinter = additionalFieldPrinter;
	}

	@Override
	public void print(Field element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		TypeArgumentablePrinter.print(element, writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			ExpressionPrinter.print(element.getInitialValue(), writer);
		}
		for (AdditionalField f : element.getAdditionalFields()) {
			writer.append(", ");
			AdditionalFieldPrinter.print(f, writer);
		}
		writer.append(";\n\n");
	}

}
