package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.statements.Switch;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.UnaryModificationExpressionChildPrinterInt;

class UnaryModificationExpressionChildPrinter implements Printer<UnaryModificationExpressionChild>, UnaryModificationExpressionChildPrinterInt {

	private final SwitchPrinter SwitchPrinter;
	private final CastExpressionPrinter CastExpressionPrinter;
	private final MethodReferenceExpressionPrinter MethodReferenceExpressionPrinter;
	private final MethodReferenceExpressionChildPrinter MethodReferenceExpressionChildPrinter;

	@Inject
	public UnaryModificationExpressionChildPrinter(jamopp.printer.implementation.SwitchPrinter switchPrinter,
			CastExpressionPrinter castExpressionPrinter,
			MethodReferenceExpressionPrinter methodReferenceExpressionPrinter,
			MethodReferenceExpressionChildPrinter methodReferenceExpressionChildPrinter) {
		super();
		SwitchPrinter = switchPrinter;
		CastExpressionPrinter = castExpressionPrinter;
		MethodReferenceExpressionPrinter = methodReferenceExpressionPrinter;
		MethodReferenceExpressionChildPrinter = methodReferenceExpressionChildPrinter;
	}

	@Override
	public void print(UnaryModificationExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof Switch) {
			SwitchPrinter.print((Switch) element, writer);
		} else if (element instanceof CastExpression) {
			CastExpressionPrinter.print((CastExpression) element, writer);
		} else if (element instanceof MethodReferenceExpression) {
			MethodReferenceExpressionPrinter.print((MethodReferenceExpression) element, writer);
		} else {
			MethodReferenceExpressionChildPrinter.print((MethodReferenceExpressionChild) element, writer);
		}
	}

}
