package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExpressionList;
import org.emftext.language.java.statements.ForLoopInitializer;
import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ForLoopInitializerPrinterInt;
import jamopp.printer.interfaces.printer.LocalVariablePrinterInt;

public class ForLoopInitializerPrinterImpl implements ForLoopInitializerPrinterInt {

	private final LocalVariablePrinterInt LocalVariablePrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public ForLoopInitializerPrinterImpl(LocalVariablePrinterInt localVariablePrinter,
			ExpressionPrinterInt expressionPrinter) {
		LocalVariablePrinter = localVariablePrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ForLoopInitializer element, BufferedWriter writer) throws IOException {
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
