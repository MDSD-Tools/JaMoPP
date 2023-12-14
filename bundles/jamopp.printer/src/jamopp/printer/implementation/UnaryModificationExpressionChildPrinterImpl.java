package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.statements.Switch;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.CastExpressionPrinterInt;
import jamopp.printer.interfaces.printer.MethodReferenceExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.MethodReferenceExpressionPrinterInt;
import jamopp.printer.interfaces.printer.SwitchPrinterInt;
import jamopp.printer.interfaces.printer.UnaryModificationExpressionChildPrinterInt;

public class UnaryModificationExpressionChildPrinterImpl implements UnaryModificationExpressionChildPrinterInt {

	private final SwitchPrinterInt SwitchPrinter;
	private final CastExpressionPrinterInt CastExpressionPrinter;
	private final MethodReferenceExpressionPrinterInt MethodReferenceExpressionPrinter;
	private final MethodReferenceExpressionChildPrinterInt MethodReferenceExpressionChildPrinter;

	@Inject
	public UnaryModificationExpressionChildPrinterImpl(SwitchPrinterInt switchPrinter,
			CastExpressionPrinterInt castExpressionPrinter,
			MethodReferenceExpressionPrinterInt methodReferenceExpressionPrinter,
			MethodReferenceExpressionChildPrinterInt methodReferenceExpressionChildPrinter) {
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
