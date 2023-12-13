package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.RelationExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.InstanceOfExpressionChildPrinterInt;

public class InstanceOfExpressionChildPrinter implements InstanceOfExpressionChildPrinterInt {

	private final RelationExpressionPrinter RelationExpressionPrinter;
	private final RelationExpressionChildPrinter RelationExpressionChildPrinter;

	@Inject
	public InstanceOfExpressionChildPrinter(
			jamopp.printer.implementation.RelationExpressionPrinter relationExpressionPrinter,
			jamopp.printer.implementation.RelationExpressionChildPrinter relationExpressionChildPrinter) {
		super();
		RelationExpressionPrinter = relationExpressionPrinter;
		RelationExpressionChildPrinter = relationExpressionChildPrinter;
	}

	@Override
	public void print(InstanceOfExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof RelationExpression) {
			RelationExpressionPrinter.print((RelationExpression) element, writer);
		} else {
			RelationExpressionChildPrinter.print((RelationExpressionChild) element, writer);
		}
	}

}
