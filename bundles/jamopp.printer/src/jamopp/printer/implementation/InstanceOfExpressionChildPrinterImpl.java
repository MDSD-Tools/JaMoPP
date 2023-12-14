package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.RelationExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InstanceOfExpressionChildPrinterImpl implements Printer<InstanceOfExpressionChild> {

	private final Printer<RelationExpressionChild> RelationExpressionChildPrinter;
	private final Printer<RelationExpression> RelationExpressionPrinter;

	@Inject
	public InstanceOfExpressionChildPrinterImpl(Printer<RelationExpression> relationExpressionPrinter,
			Printer<RelationExpressionChild> relationExpressionChildPrinter) {
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
