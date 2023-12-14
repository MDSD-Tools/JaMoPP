package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.InstanceOfExpression;
import org.emftext.language.java.expressions.InstanceOfExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.printer.EqualityExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.InstanceOfExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.InstanceOfExpressionPrinterInt;

public class EqualityExpressionChildPrinterImpl implements EqualityExpressionChildPrinterInt {

	private final InstanceOfExpressionPrinterInt InstanceOfExpressionPrinter;
	private final InstanceOfExpressionChildPrinterInt InstanceOfExpressionChildPrinter;

	@Inject
	public EqualityExpressionChildPrinterImpl(InstanceOfExpressionPrinterInt instanceOfExpressionPrinter,
			InstanceOfExpressionChildPrinterInt instanceOfExpressionChildPrinter) {
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
