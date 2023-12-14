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

public class InterfaceMethodPrinterImpl implements Printer<InterfaceMethod> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<AnnotationValue> annotationValuePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<ExceptionThrower> exceptionThrowerPrinter;
	private final Printer<Parametrizable> parametrizablePrinter;
	private final Printer<Statement> statementPrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public InterfaceMethodPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeParametrizable> typeParametrizablePrinter, Printer<TypeReference> typeReferencePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Parametrizable> parametrizablePrinter,
			Printer<ExceptionThrower> exceptionThrowerPrinter, Printer<AnnotationValue> annotationValuePrinter,
			Printer<Statement> statementPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.parametrizablePrinter = parametrizablePrinter;
		this.exceptionThrowerPrinter = exceptionThrowerPrinter;
		this.annotationValuePrinter = annotationValuePrinter;
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(InterfaceMethod element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		this.typeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		this.parametrizablePrinter.print(element, writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		this.exceptionThrowerPrinter.print(element, writer);
		writer.append(" ");
		if (element.getDefaultValue() != null) {
			writer.append("default ");
			this.annotationValuePrinter.print(element.getDefaultValue(), writer);
		}
		this.statementPrinter.print(element.getStatement(), writer);
		writer.append("\n");
	}

}
