package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ArrayConstructorReferenceExpression;
import org.emftext.language.java.expressions.ClassTypeConstructorReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.PrimaryExpressionReferenceExpression;

class MethodReferenceExpressionPrinter {

	static void print(MethodReferenceExpression element, BufferedWriter writer)
			throws IOException {
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
