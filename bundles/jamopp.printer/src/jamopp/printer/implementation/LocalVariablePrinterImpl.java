package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.variables.AdditionalLocalVariable;
import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.LocalVariablePrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class LocalVariablePrinterImpl implements LocalVariablePrinterInt {

	private final Printer<AdditionalLocalVariable> AdditionalLocalVariablePrinter;
	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<Expression> ExpressionPrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public LocalVariablePrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			TypeReferencePrinterInt typeReferencePrinter, TypeArgumentablePrinterInt typeArgumentablePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Expression> expressionPrinter,
			Printer<AdditionalLocalVariable> additionalLocalVariablePrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ExpressionPrinter = expressionPrinter;
		AdditionalLocalVariablePrinter = additionalLocalVariablePrinter;
	}

	@Override
	public void print(LocalVariable element, BufferedWriter writer) throws IOException {
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
		for (AdditionalLocalVariable var : element.getAdditionalLocalVariables()) {
			writer.append(", ");
			AdditionalLocalVariablePrinter.print(var, writer);
		}
	}

}
