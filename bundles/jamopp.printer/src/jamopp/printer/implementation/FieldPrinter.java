package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Field;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.FieldPrinterInt;

public class FieldPrinter implements FieldPrinterInt {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final ExpressionPrinter ExpressionPrinter;
	private final AdditionalFieldPrinter AdditionalFieldPrinter;

	@Inject
	public FieldPrinter(jamopp.printer.implementation.AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter,
			jamopp.printer.implementation.AdditionalFieldPrinter additionalFieldPrinter) {
		super();
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
