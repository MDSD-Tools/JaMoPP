package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.InterfaceMethod;

import jamopp.printer.interfaces.Printer;

class InterfaceMethodPrinter implements Printer<InterfaceMethod>{

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinter TypeParametrizablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final ParametrizablePrinter ParametrizablePrinter;
	private final ExceptionThrowerPrinter ExceptionThrowerPrinter;
	private final AnnotationValuePrinter AnnotationValuePrinter;
	private final StatementPrinter StatementPrinter;
	
	public void print(InterfaceMethod element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.print(element, writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		ExceptionThrowerPrinter.print(element, writer);
		writer.append(" ");
		if (element.getDefaultValue() != null) {
			writer.append("default ");
			AnnotationValuePrinter.print(element.getDefaultValue(), writer);
		}
		StatementPrinter.print(element.getStatement(), writer);
		writer.append("\n");
	}

}
