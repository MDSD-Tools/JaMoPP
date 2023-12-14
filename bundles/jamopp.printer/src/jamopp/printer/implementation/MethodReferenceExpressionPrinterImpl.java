package jamopp.printer.implementation;

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

import jamopp.printer.interfaces.Printer;

public class MethodReferenceExpressionPrinterImpl implements Printer<MethodReferenceExpression> {

	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<CallTypeArgumentable> CallTypeArgumentablePrinter;
	private final Printer<MethodReferenceExpressionChild> MethodReferenceExpressionChildPrinter;
	private final Printer<Reference> ReferencePrinter;
	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public MethodReferenceExpressionPrinterImpl(
			Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter,
			Printer<CallTypeArgumentable> callTypeArgumentablePrinter, Printer<Reference> referencePrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		MethodReferenceExpressionChildPrinter = methodReferenceExpressionChildPrinter;
		CallTypeArgumentablePrinter = callTypeArgumentablePrinter;
		ReferencePrinter = referencePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(MethodReferenceExpression element, BufferedWriter writer) throws IOException {
		if (element instanceof PrimaryExpressionReferenceExpression ref) {
			MethodReferenceExpressionChildPrinter.print(ref.getChild(), writer);
			if (ref.getMethodReference() != null) {
				writer.append("::");
				CallTypeArgumentablePrinter.print(ref, writer);
				ReferencePrinter.print(ref.getMethodReference(), writer);
			}
		} else if (element instanceof ClassTypeConstructorReferenceExpression ref) {
			TypeReferencePrinter.print(ref.getTypeReference(), writer);
			writer.append("::");
			CallTypeArgumentablePrinter.print(ref, writer);
			writer.append("new");
		} else {
			var ref = (ArrayConstructorReferenceExpression) element;
			TypeReferencePrinter.print(ref.getTypeReference(), writer);
			ArrayDimensionsPrinter.print(ref.getArrayDimensionsBefore(), writer);
			ArrayDimensionsPrinter.print(ref.getArrayDimensionsAfter(), writer);
			writer.append("::new");
		}
	}

}
