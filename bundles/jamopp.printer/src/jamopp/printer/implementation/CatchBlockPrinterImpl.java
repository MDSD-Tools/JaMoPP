package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.CatchBlock;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.CatchBlockPrinterInt;
import jamopp.printer.interfaces.printer.CatchParameterPrinterInt;

public class CatchBlockPrinterImpl implements Printer<CatchBlock> {

	private final Printer<CatchParameter> CatchParameterPrinter;
	private final Printer<Block> BlockPrinter;

	@Inject
	public CatchBlockPrinterImpl(Printer<CatchParameter> catchParameterPrinter, Printer<Block> blockPrinter) {
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
