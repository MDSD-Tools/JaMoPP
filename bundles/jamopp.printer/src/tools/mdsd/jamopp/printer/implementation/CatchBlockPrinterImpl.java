package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.CatchBlock;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CatchBlockPrinterImpl implements Printer<CatchBlock> {

	private final Printer<Block> blockPrinter;
	private final Printer<CatchParameter> catchParameterPrinter;

	@Inject
	public CatchBlockPrinterImpl(Printer<CatchParameter> catchParameterPrinter, Printer<Block> blockPrinter) {
		this.catchParameterPrinter = catchParameterPrinter;
		this.blockPrinter = blockPrinter;
	}

	@Override
	public void print(CatchBlock element, BufferedWriter writer) throws IOException {
		writer.append("catch(");
		this.catchParameterPrinter.print((CatchParameter) element.getParameter(), writer);
		writer.append(")");
		this.blockPrinter.print(element.getBlock(), writer);
	}

}
