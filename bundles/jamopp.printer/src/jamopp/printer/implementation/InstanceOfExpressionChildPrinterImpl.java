package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.RelationExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InstanceOfExpressionChildPrinterImpl implements Printer<InstanceOfExpressionChild> {

	private final Printer<RelationExpressionChild> relationExpressionChildPrinter;
	private final Printer<RelationExpression> relationExpressionPrinter;

	@Inject
	public InstanceOfExpressionChildPrinterImpl(Printer<RelationExpression> relationExpressionPrinter,
			Printer<RelationExpressionChild> relationExpressionChildPrinter) {
		this.relationExpressionPrinter = relationExpressionPrinter;
		this.relationExpressionChildPrinter = relationExpressionChildPrinter;
	}

	@Override
	public void print(InstanceOfExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof RelationExpression) {
			this.relationExpressionPrinter.print((RelationExpression) element, writer);
		} else {
			this.relationExpressionChildPrinter.print((RelationExpressionChild) element, writer);
		}
	}

}
