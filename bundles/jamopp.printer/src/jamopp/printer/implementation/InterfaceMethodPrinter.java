package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.InterfaceMethod;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.AnnotationValuePrinterInt;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.ExceptionThrowerPrinterInt;
import jamopp.printer.interfaces.printer.InterfaceMethodPrinterInt;
import jamopp.printer.interfaces.printer.ParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;
import jamopp.printer.interfaces.printer.TypeParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class InterfaceMethodPrinter implements InterfaceMethodPrinterInt {

	private final AnnotableAndModifiablePrinterInt AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinterInt TypeParametrizablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;
	private final ParametrizablePrinterInt ParametrizablePrinter;
	private final ExceptionThrowerPrinterInt ExceptionThrowerPrinter;
	private final AnnotationValuePrinterInt AnnotationValuePrinter;
	private final StatementPrinterInt StatementPrinter;

	@Inject
	public InterfaceMethodPrinter(AnnotableAndModifiablePrinterInt annotableAndModifiablePrinter,
			TypeParametrizablePrinterInt typeParametrizablePrinter, TypeReferencePrinterInt typeReferencePrinter,
			ArrayDimensionsPrinterInt arrayDimensionsPrinter, ParametrizablePrinterInt parametrizablePrinter,
			ExceptionThrowerPrinterInt exceptionThrowerPrinter, AnnotationValuePrinterInt annotationValuePrinter,
			StatementPrinterInt statementPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeParametrizablePrinter = typeParametrizablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ParametrizablePrinter = parametrizablePrinter;
		ExceptionThrowerPrinter = exceptionThrowerPrinter;
		AnnotationValuePrinter = annotationValuePrinter;
		StatementPrinter = statementPrinter;
	}

	@Override
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
