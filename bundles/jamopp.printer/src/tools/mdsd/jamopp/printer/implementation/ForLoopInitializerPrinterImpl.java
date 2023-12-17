package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.ExpressionList;
import org.emftext.language.java.statements.ForLoopInitializer;
import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ForLoopInitializerPrinterImpl implements Printer<ForLoopInitializer> {

	private final Printer<Expression> expressionPrinter;
	private final Printer<LocalVariable> localVariablePrinter;

	@Inject
	public ForLoopInitializerPrinterImpl(Printer<LocalVariable> localVariablePrinter,
			Printer<Expression> expressionPrinter) {
		this.localVariablePrinter = localVariablePrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ForLoopInitializer element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			this.localVariablePrinter.print((LocalVariable) element, writer);
		} else {
			var list = (ExpressionList) element;
			for (var index = 0; index < list.getExpressions().size(); index++) {
				this.expressionPrinter.print(list.getExpressions().get(index), writer);
				if (index < list.getExpressions().size() - 1) {
					writer.append(", ");
				}
			}
		}
	}

}
