package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.statements.Switch;

public class UnaryModificationExpressionChildPrinter {

	static void printUnaryModificationExpressionChild(UnaryModificationExpressionChild element,
			BufferedWriter writer) throws IOException {
		if (element instanceof Switch) {
			SwitchPrint.printSwitch((Switch) element, writer);
		} else if (element instanceof CastExpression) {
			CastExpressionPrinter.printCastExpression((CastExpression) element, writer);
		} else if (element instanceof MethodReferenceExpression) {
			MethodReferenceExpressionPrinter.printMethodReferenceExpression((MethodReferenceExpression) element, writer);
		} else {
			MethodReferenceExpressionChildPrinter.printMethodReferenceExpressionChild((MethodReferenceExpressionChild) element, writer);
		}
	}

}
