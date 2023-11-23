package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.statements.Switch;

class UnaryModificationExpressionChildPrinter {

	static void print(UnaryModificationExpressionChild element,
			BufferedWriter writer) throws IOException {
		if (element instanceof Switch) {
			SwitchPrint.print((Switch) element, writer);
		} else if (element instanceof CastExpression) {
			CastExpressionPrinter.print((CastExpression) element, writer);
		} else if (element instanceof MethodReferenceExpression) {
			MethodReferenceExpressionPrinter.print((MethodReferenceExpression) element, writer);
		} else {
			MethodReferenceExpressionChildPrinter.print((MethodReferenceExpressionChild) element, writer);
		}
	}

}
