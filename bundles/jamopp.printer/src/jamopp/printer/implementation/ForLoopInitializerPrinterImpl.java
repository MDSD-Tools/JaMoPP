package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.ExpressionList;
import org.emftext.language.java.statements.ForLoopInitializer;
import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.LocalVariablePrinterInt;

public class ForLoopInitializerPrinterImpl implements Printer<ForLoopInitializer> {

	private final Printer<Expression> ExpressionPrinter;
	private final LocalVariablePrinterInt LocalVariablePrinter;

	@Inject
	public ForLoopInitializerPrinterImpl(LocalVariablePrinterInt localVariablePrinter,
			Printer<Expression> expressionPrinter) {
		LocalVariablePrinter = localVariablePrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ForLoopInitializer element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			LocalVariablePrinter.print((LocalVariable) element, writer);
		} else {
			var list = (ExpressionList) element;
			for (var index = 0; index < list.getExpressions().size(); index++) {
				ExpressionPrinter.print(list.getExpressions().get(index), writer);
				if (index < list.getExpressions().size() - 1) {
					writer.append(", ");
				}
			}
		}
	}

}
