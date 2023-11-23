package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.references.Reference;

class MethodReferenceExpressionChildPrinter {

	static void print(MethodReferenceExpressionChild element,
			BufferedWriter writer) throws IOException {
		if (element instanceof Literal) {
			LiteralPrinter.print((Literal) element, writer);
		} else {
			ReferencePrinter.print((Reference) element, writer);
		}
	}

}
