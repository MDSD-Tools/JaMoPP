package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.ArrayConstructorReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.ClassTypeConstructorReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpressionReferenceExpression;
import tools.mdsd.jamopp.model.java.generics.CallTypeArgumentable;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MethodReferenceExpressionPrinterImpl implements Printer<MethodReferenceExpression> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<CallTypeArgumentable> callTypeArgumentablePrinter;
	private final Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter;
	private final Printer<Reference> referencePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public MethodReferenceExpressionPrinterImpl(
			final Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter,
			final Printer<CallTypeArgumentable> callTypeArgumentablePrinter, final Printer<Reference> referencePrinter,
			final Printer<TypeReference> typeReferencePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.methodReferenceExpressionChildPrinter = methodReferenceExpressionChildPrinter;
		this.callTypeArgumentablePrinter = callTypeArgumentablePrinter;
		this.referencePrinter = referencePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(final MethodReferenceExpression element, final BufferedWriter writer) throws IOException {
		if (element instanceof final PrimaryExpressionReferenceExpression ref) {
			methodReferenceExpressionChildPrinter.print(ref.getChild(), writer);
			if (ref.getMethodReference() != null) {
				writer.append("::");
				callTypeArgumentablePrinter.print(ref, writer);
				referencePrinter.print(ref.getMethodReference(), writer);
			}
		} else if (element instanceof final ClassTypeConstructorReferenceExpression ref) {
			typeReferencePrinter.print(ref.getTypeReference(), writer);
			writer.append("::");
			callTypeArgumentablePrinter.print(ref, writer);
			writer.append("new");
		} else {
			final var ref = (ArrayConstructorReferenceExpression) element;
			typeReferencePrinter.print(ref.getTypeReference(), writer);
			arrayDimensionsPrinter.print(ref.getArrayDimensionsBefore(), writer);
			arrayDimensionsPrinter.print(ref.getArrayDimensionsAfter(), writer);
			writer.append("::new");
		}
	}

}
