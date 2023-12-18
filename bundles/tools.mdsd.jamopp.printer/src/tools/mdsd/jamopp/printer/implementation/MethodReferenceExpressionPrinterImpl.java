package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.ArrayConstructorReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.ClassTypeConstructorReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpressionReferenceExpression;
import tools.mdsd.jamopp.model.java.generics.CallTypeArgumentable;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MethodReferenceExpressionPrinterImpl implements Printer<MethodReferenceExpression> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<CallTypeArgumentable> callTypeArgumentablePrinter;
	private final Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter;
	private final Printer<Reference> referencePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public MethodReferenceExpressionPrinterImpl(
			Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter,
			Printer<CallTypeArgumentable> callTypeArgumentablePrinter, Printer<Reference> referencePrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.methodReferenceExpressionChildPrinter = methodReferenceExpressionChildPrinter;
		this.callTypeArgumentablePrinter = callTypeArgumentablePrinter;
		this.referencePrinter = referencePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(MethodReferenceExpression element, BufferedWriter writer) throws IOException {
		if (element instanceof PrimaryExpressionReferenceExpression ref) {
			this.methodReferenceExpressionChildPrinter.print(ref.getChild(), writer);
			if (ref.getMethodReference() != null) {
				writer.append("::");
				this.callTypeArgumentablePrinter.print(ref, writer);
				this.referencePrinter.print(ref.getMethodReference(), writer);
			}
		} else if (element instanceof ClassTypeConstructorReferenceExpression ref) {
			this.typeReferencePrinter.print(ref.getTypeReference(), writer);
			writer.append("::");
			this.callTypeArgumentablePrinter.print(ref, writer);
			writer.append("new");
		} else {
			var ref = (ArrayConstructorReferenceExpression) element;
			this.typeReferencePrinter.print(ref.getTypeReference(), writer);
			this.arrayDimensionsPrinter.print(ref.getArrayDimensionsBefore(), writer);
			this.arrayDimensionsPrinter.print(ref.getArrayDimensionsAfter(), writer);
			writer.append("::new");
		}
	}

}
