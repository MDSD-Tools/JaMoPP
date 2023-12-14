package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.generics.TypeParametrizable;
import org.emftext.language.java.members.ExceptionThrower;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.parameters.Parametrizable;
import org.emftext.language.java.statements.Statement;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InterfaceMethodPrinterImpl implements Printer<InterfaceMethod>  {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<AnnotationValue> AnnotationValuePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<ExceptionThrower> ExceptionThrowerPrinter;
	private final Printer<Parametrizable> ParametrizablePrinter;
	private final Printer<Statement> StatementPrinter;
	private final Printer<TypeParametrizable> TypeParametrizablePrinter;
	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public InterfaceMethodPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeParametrizable> typeParametrizablePrinter, Printer<TypeReference> typeReferencePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Parametrizable> parametrizablePrinter,
			Printer<ExceptionThrower> exceptionThrowerPrinter, Printer<AnnotationValue> annotationValuePrinter,
			Printer<Statement> statementPrinter) {
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
