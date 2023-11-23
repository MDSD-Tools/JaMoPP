package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpressionChild;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.RelationExpressionChild;

public class InstanceOfExpressionChildPrinter {

	static void printInstanceOfExpressionChild(InstanceOfExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof RelationExpression) {
			RelationExpressionPrinter.printRelationExpression((RelationExpression) element, writer);
		} else {
			RelationExpressionChildPrinter.printRelationExpressionChild((RelationExpressionChild) element, writer);
		}
	}

}
