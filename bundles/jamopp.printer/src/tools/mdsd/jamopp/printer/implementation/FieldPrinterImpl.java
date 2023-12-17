package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class FieldPrinterImpl implements Printer<Field> {

	private final Printer<AdditionalField> additionalFieldPrinter;
	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<Expression> expressionPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public FieldPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<TypeArgumentable> typeArgumentablePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Expression> expressionPrinter,
			Printer<AdditionalField> additionalFieldPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.expressionPrinter = expressionPrinter;
		this.additionalFieldPrinter = additionalFieldPrinter;
	}

	@Override
	public void print(Field element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		this.typeArgumentablePrinter.print(element, writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			this.expressionPrinter.print(element.getInitialValue(), writer);
		}
		for (AdditionalField f : element.getAdditionalFields()) {
			writer.append(", ");
			this.additionalFieldPrinter.print(f, writer);
		}
		writer.append(";\n\n");
	}

}
