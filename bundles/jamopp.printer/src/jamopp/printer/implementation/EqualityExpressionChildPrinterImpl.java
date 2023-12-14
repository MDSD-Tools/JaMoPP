package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.InstanceOfExpression;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class EqualityExpressionChildPrinterImpl implements Printer<EqualityExpressionChild> {

	private final Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter;
	private final Printer<InstanceOfExpression> instanceOfExpressionPrinter;

	@Inject
	public EqualityExpressionChildPrinterImpl(Printer<InstanceOfExpression> instanceOfExpressionPrinter,
			Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter) {
		this.instanceOfExpressionPrinter = instanceOfExpressionPrinter;
		this.instanceOfExpressionChildPrinter = instanceOfExpressionChildPrinter;
	}

	@Override
	public void print(EqualityExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof InstanceOfExpression) {
			this.instanceOfExpressionPrinter.print((InstanceOfExpression) element, writer);
		} else {
			this.instanceOfExpressionChildPrinter.print((InstanceOfExpressionChild) element, writer);
		}
	}

}
