package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.RelationExpressionChild;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.InstanceOfExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.RelationExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.RelationExpressionPrinterInt;

public class InstanceOfExpressionChildPrinterImpl implements InstanceOfExpressionChildPrinterInt {

	private final RelationExpressionPrinterInt RelationExpressionPrinter;
	private final RelationExpressionChildPrinterInt RelationExpressionChildPrinter;

	@Inject
	public InstanceOfExpressionChildPrinterImpl(RelationExpressionPrinterInt relationExpressionPrinter,
			RelationExpressionChildPrinterInt relationExpressionChildPrinter) {
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
