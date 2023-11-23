package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.references.Reference;

public class MethodReferenceExpressionChildPrinter {

	static void printMethodReferenceExpressionChild(MethodReferenceExpressionChild element,
			BufferedWriter writer) throws IOException {
		if (element instanceof Literal) {
			LiteralPrinter.printLiteral((Literal) element, writer);
		} else {
			ReferencePrinter.printReference((Reference) element, writer);
		}
	}

}
