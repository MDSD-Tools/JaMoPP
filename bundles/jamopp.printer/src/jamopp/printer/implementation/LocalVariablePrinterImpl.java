package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.variables.AdditionalLocalVariable;
import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class LocalVariablePrinterImpl implements Printer<LocalVariable> {

	private final Printer<AdditionalLocalVariable> additionalLocalVariablePrinter;
	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<Expression> expressionPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public LocalVariablePrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<TypeArgumentable> typeArgumentablePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Expression> expressionPrinter,
			Printer<AdditionalLocalVariable> additionalLocalVariablePrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.expressionPrinter = expressionPrinter;
		this.additionalLocalVariablePrinter = additionalLocalVariablePrinter;
	}

	@Override
	public void print(LocalVariable element, BufferedWriter writer) throws IOException {
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
		for (AdditionalLocalVariable var : element.getAdditionalLocalVariables()) {
			writer.append(", ");
			this.additionalLocalVariablePrinter.print(var, writer);
		}
	}

}
