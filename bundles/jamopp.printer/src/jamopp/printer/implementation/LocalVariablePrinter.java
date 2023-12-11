package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.variables.AdditionalLocalVariable;
import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class LocalVariablePrinter implements Printer<LocalVariable> {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final ExpressionPrinter ExpressionPrinter;
	private final AdditionalLocalVariablePrinter AdditionalLocalVariablePrinter;

	@Inject
	public LocalVariablePrinter(
			jamopp.printer.implementation.AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter,
			jamopp.printer.implementation.AdditionalLocalVariablePrinter additionalLocalVariablePrinter) {
		super();
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ExpressionPrinter = expressionPrinter;
		AdditionalLocalVariablePrinter = additionalLocalVariablePrinter;
	}

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
