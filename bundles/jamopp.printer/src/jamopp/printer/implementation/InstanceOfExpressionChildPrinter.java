package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.RelationExpressionChild;

import jamopp.printer.interfaces.Printer;

class InstanceOfExpressionChildPrinter implements Printer<InstanceOfExpressionChild>{

	public void print(InstanceOfExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof RelationExpression) {
			RelationExpressionPrinter.print((RelationExpression) element, writer);
		} else {
			RelationExpressionChildPrinter.print((RelationExpressionChild) element, writer);
		}
	}

}
