package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.statements.CatchBlock;

import jamopp.printer.interfaces.Printer;

class CatchBlockPrinter implements Printer<CatchBlock>{

	private final CatchParameterPrinter CatchParameterPrinter;
	private final BlockPrinter BlockPrinter;
	
	public void print(CatchBlock element, BufferedWriter writer) throws IOException {
		writer.append("catch(");
		CatchParameterPrinter.print((CatchParameter) element.getParameter(), writer);
		writer.append(")");
		BlockPrinter.print(element.getBlock(), writer);
	}

}
