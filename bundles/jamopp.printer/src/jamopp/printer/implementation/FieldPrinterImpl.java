package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class FieldPrinterImpl implements Printer<Field> {

	private final Printer<AdditionalField> AdditionalFieldPrinter;
	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<Expression> ExpressionPrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public FieldPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			TypeReferencePrinterInt typeReferencePrinter, TypeArgumentablePrinterInt typeArgumentablePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Expression> expressionPrinter,
			Printer<AdditionalField> additionalFieldPrinter) {
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
