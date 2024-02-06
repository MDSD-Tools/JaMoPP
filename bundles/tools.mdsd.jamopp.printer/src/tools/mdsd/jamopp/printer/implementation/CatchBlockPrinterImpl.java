package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.CatchBlock;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CatchBlockPrinterImpl implements Printer<CatchBlock> {

	private final Printer<Block> blockPrinter;
	private final Printer<CatchParameter> catchParameterPrinter;

	@Inject
	public CatchBlockPrinterImpl(final Printer<CatchParameter> catchParameterPrinter,
			final Printer<Block> blockPrinter) {
		this.catchParameterPrinter = catchParameterPrinter;
		this.blockPrinter = blockPrinter;
	}

	@Override
	public void print(final CatchBlock element, final BufferedWriter writer) throws IOException {
		writer.append("catch(");
		catchParameterPrinter.print((CatchParameter) element.getParameter(), writer);
		writer.append(")");
		blockPrinter.print(element.getBlock(), writer);
	}

}
