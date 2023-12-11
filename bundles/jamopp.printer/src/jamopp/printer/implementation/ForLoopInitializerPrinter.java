package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExpressionList;
import org.emftext.language.java.statements.ForLoopInitializer;
import org.emftext.language.java.variables.LocalVariable;

import jamopp.printer.interfaces.Printer;

class ForLoopInitializerPrinter implements Printer<ForLoopInitializer>{

	static void print(ForLoopInitializer element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			LocalVariablePrinter.print((LocalVariable) element, writer);
		} else {
			ExpressionList list = (ExpressionList) element;
			for (int index = 0; index < list.getExpressions().size(); index++) {
				ExpressionPrinter.print(list.getExpressions().get(index), writer);
				if (index < list.getExpressions().size() - 1) {
					writer.append(", ");
				}
			}
		}
	}

}
