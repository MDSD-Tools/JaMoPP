package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.YieldStatement;

import jamopp.printer.interfaces.Printer;

class YieldStatementPrinter implements Printer<YieldStatement>{

	public void print(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		ExpressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
