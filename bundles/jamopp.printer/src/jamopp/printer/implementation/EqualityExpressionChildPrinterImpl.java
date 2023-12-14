package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.InstanceOfExpression;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class EqualityExpressionChildPrinterImpl implements Printer<EqualityExpressionChild> {

	private final Printer<InstanceOfExpressionChild> InstanceOfExpressionChildPrinter;
	private final Printer<InstanceOfExpression> InstanceOfExpressionPrinter;

	@Inject
	public EqualityExpressionChildPrinterImpl(Printer<InstanceOfExpression> instanceOfExpressionPrinter,
			Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter) {
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
