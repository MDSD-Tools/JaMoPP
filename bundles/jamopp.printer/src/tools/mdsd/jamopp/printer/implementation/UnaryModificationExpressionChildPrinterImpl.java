package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.expressions.MethodReferenceExpression;
import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.statements.Switch;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryModificationExpressionChildPrinterImpl implements Printer<UnaryModificationExpressionChild> {

	private final Printer<CastExpression> castExpressionPrinter;
	private final Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter;
	private final Printer<MethodReferenceExpression> methodReferenceExpressionPrinter;
	private final Printer<Switch> switchPrinter;

	@Inject
	public UnaryModificationExpressionChildPrinterImpl(Printer<Switch> switchPrinter,
			Printer<CastExpression> castExpressionPrinter,
			Printer<MethodReferenceExpression> methodReferenceExpressionPrinter,
			Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter) {
		this.switchPrinter = switchPrinter;
		this.castExpressionPrinter = castExpressionPrinter;
		this.methodReferenceExpressionPrinter = methodReferenceExpressionPrinter;
		this.methodReferenceExpressionChildPrinter = methodReferenceExpressionChildPrinter;
	}

	@Override
	public void print(UnaryModificationExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof Switch) {
			this.switchPrinter.print((Switch) element, writer);
		} else if (element instanceof CastExpression) {
			this.castExpressionPrinter.print((CastExpression) element, writer);
		} else if (element instanceof MethodReferenceExpression) {
			this.methodReferenceExpressionPrinter.print((MethodReferenceExpression) element, writer);
		} else {
			this.methodReferenceExpressionChildPrinter.print((MethodReferenceExpressionChild) element, writer);
		}
	}

}
