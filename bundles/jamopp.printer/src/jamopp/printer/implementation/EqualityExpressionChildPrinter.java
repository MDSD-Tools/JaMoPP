package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.InstanceOfExpression;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class EqualityExpressionChildPrinter implements Printer<EqualityExpressionChild> {

	private final InstanceOfExpressionPrinter InstanceOfExpressionPrinter;
	private final InstanceOfExpressionChildPrinter InstanceOfExpressionChildPrinter;

	@Inject
	public EqualityExpressionChildPrinter(
			jamopp.printer.implementation.InstanceOfExpressionPrinter instanceOfExpressionPrinter,
			jamopp.printer.implementation.InstanceOfExpressionChildPrinter instanceOfExpressionChildPrinter) {
		super();
		InstanceOfExpressionPrinter = instanceOfExpressionPrinter;
		InstanceOfExpressionChildPrinter = instanceOfExpressionChildPrinter;
	}

	@Override
	public void print(EqualityExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof InstanceOfExpression) {
			InstanceOfExpressionPrinter.print((InstanceOfExpression) element, writer);
		} else {
			InstanceOfExpressionChildPrinter.print((InstanceOfExpressionChild) element, writer);
		}
	}

}
