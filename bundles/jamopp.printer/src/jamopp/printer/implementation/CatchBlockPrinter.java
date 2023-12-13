package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.statements.CatchBlock;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.CatchBlockPrinterInt;

class CatchBlockPrinter implements CatchBlockPrinterInt {

	private final CatchParameterPrinter CatchParameterPrinter;
	private final BlockPrinter BlockPrinter;

	@Inject
	public CatchBlockPrinter(jamopp.printer.implementation.CatchParameterPrinter catchParameterPrinter,
			jamopp.printer.implementation.BlockPrinter blockPrinter) {
		super();
		CatchParameterPrinter = catchParameterPrinter;
		BlockPrinter = blockPrinter;
	}

	@Override
	public void print(CatchBlock element, BufferedWriter writer) throws IOException {
		writer.append("catch(");
		CatchParameterPrinter.print((CatchParameter) element.getParameter(), writer);
		writer.append(")");
		BlockPrinter.print(element.getBlock(), writer);
	}

}
