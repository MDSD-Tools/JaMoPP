package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ArrayConstructorReferenceExpression;
import org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression;

public class MethodReferenceExpressionPrinter {

	static void printMethodReferenceExpression(MethodReferenceExpression element, BufferedWriter writer)
			throws IOException {
		if (element instanceof PrimaryExpressionReferenceExpression ref) {
			MethodReferenceExpressionChildPrinter.printMethodReferenceExpressionChild(ref.getChild(), writer);
			if (ref.getMethodReference() != null) {
				writer.append("::");
				CallTypeArgumentablePrinter.printCallTypeArgumentable(ref, writer);
				ReferencePrinter.printReference(ref.getMethodReference(), writer);
			}
		} else if (element instanceof ClassTypeConstructorReferenceExpression ref) {
			TypeReferencePrinter.printTypeReference(ref.getTypeReference(), writer);
			writer.append("::");
			CallTypeArgumentablePrinter.printCallTypeArgumentable(ref, writer);
			writer.append("new");
		} else {
			ArrayConstructorReferenceExpression ref = (ArrayConstructorReferenceExpression) element;
			TypeReferencePrinter.printTypeReference(ref.getTypeReference(), writer);
			ArrayDimensionsPrinter.printArrayDimensions(ref.getArrayDimensionsBefore(), writer);
			ArrayDimensionsPrinter.printArrayDimensions(ref.getArrayDimensionsAfter(), writer);
			writer.append("::new");
		}
	}

}
