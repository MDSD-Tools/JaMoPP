package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ArrayConstructorReferenceExpression;
import org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.MethodReferenceExpressionPrinterInt;

public class MethodReferenceExpressionPrinter implements MethodReferenceExpressionPrinterInt {

	private final MethodReferenceExpressionChildPrinter MethodReferenceExpressionChildPrinter;
	private final CallTypeArgumentablePrinter CallTypeArgumentablePrinter;
	private final ReferencePrinter ReferencePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;

	@Inject
	public MethodReferenceExpressionPrinter(
			jamopp.printer.implementation.MethodReferenceExpressionChildPrinter methodReferenceExpressionChildPrinter,
			jamopp.printer.implementation.CallTypeArgumentablePrinter callTypeArgumentablePrinter,
			jamopp.printer.implementation.ReferencePrinter referencePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter) {
		super();
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
			ArrayConstructorReferenceExpression ref = (ArrayConstructorReferenceExpression) element;
			TypeReferencePrinter.print(ref.getTypeReference(), writer);
			ArrayDimensionsPrinter.print(ref.getArrayDimensionsBefore(), writer);
			ArrayDimensionsPrinter.print(ref.getArrayDimensionsAfter(), writer);
			writer.append("::new");
		}
	}

}
