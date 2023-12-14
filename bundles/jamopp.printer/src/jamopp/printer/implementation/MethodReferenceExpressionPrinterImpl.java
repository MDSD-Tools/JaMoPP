package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ArrayConstructorReferenceExpression;
import org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.CallTypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.MethodReferenceExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.MethodReferenceExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ReferencePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class MethodReferenceExpressionPrinterImpl implements MethodReferenceExpressionPrinterInt {

	private final MethodReferenceExpressionChildPrinterInt MethodReferenceExpressionChildPrinter;
	private final CallTypeArgumentablePrinterInt CallTypeArgumentablePrinter;
	private final ReferencePrinterInt ReferencePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;

	@Inject
	public MethodReferenceExpressionPrinterImpl(
			MethodReferenceExpressionChildPrinterInt methodReferenceExpressionChildPrinter,
			CallTypeArgumentablePrinterInt callTypeArgumentablePrinter, ReferencePrinterInt referencePrinter,
			TypeReferencePrinterInt typeReferencePrinter, ArrayDimensionsPrinterInt arrayDimensionsPrinter) {
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
