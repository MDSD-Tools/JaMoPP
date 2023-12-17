package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.ArrayConstructorReferenceExpression;
import org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression;
import org.emftext.language.java.generics.CallTypeArgumentable;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.types.TypeReference;

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
