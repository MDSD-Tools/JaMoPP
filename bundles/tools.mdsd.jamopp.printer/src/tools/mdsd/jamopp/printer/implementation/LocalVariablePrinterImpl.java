package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class LocalVariablePrinterImpl implements Printer<LocalVariable> {

	private final Printer<AdditionalLocalVariable> additionalLocalVariablePrinter;
	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<Expression> expressionPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public LocalVariablePrinterImpl(final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			final Printer<TypeReference> typeReferencePrinter, final Printer<TypeArgumentable> typeArgumentablePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter, final Printer<Expression> expressionPrinter,
			final Printer<AdditionalLocalVariable> additionalLocalVariablePrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.expressionPrinter = expressionPrinter;
		this.additionalLocalVariablePrinter = additionalLocalVariablePrinter;
	}

	@Override
	public void print(final LocalVariable element, final BufferedWriter writer) throws IOException {
		annotableAndModifiablePrinter.print(element, writer);
		typeReferencePrinter.print(element.getTypeReference(), writer);
		typeArgumentablePrinter.print(element, writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			expressionPrinter.print(element.getInitialValue(), writer);
		}
		for (final AdditionalLocalVariable variable : element.getAdditionalLocalVariables()) {
			writer.append(", ");
			additionalLocalVariablePrinter.print(variable, writer);
		}
	}

}
