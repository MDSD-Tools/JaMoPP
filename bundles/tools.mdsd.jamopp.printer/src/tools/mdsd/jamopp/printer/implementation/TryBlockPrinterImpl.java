package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.CatchBlock;
import tools.mdsd.jamopp.model.java.statements.TryBlock;
import tools.mdsd.jamopp.model.java.variables.Resource;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TryBlockPrinterImpl implements Printer<TryBlock> {

	private final Printer<Block> blockPrinter;
	private final Printer<CatchBlock> catchBlockPrinter;
	private final Printer<Resource> resourcePrinter;

	@Inject
	public TryBlockPrinterImpl(Printer<Resource> resourcePrinter, Printer<Block> blockPrinter,
			Printer<CatchBlock> catchBlockPrinter) {
		this.resourcePrinter = resourcePrinter;
		this.blockPrinter = blockPrinter;
		this.catchBlockPrinter = catchBlockPrinter;
	}

	@Override
	public void print(TryBlock element, BufferedWriter writer) throws IOException {
		writer.append("try");
		if (!element.getResources().isEmpty()) {
			writer.append("(");
			this.resourcePrinter.print(element.getResources().get(0), writer);
			for (var index = 1; index < element.getResources().size(); index++) {
				writer.append("; ");
				this.resourcePrinter.print(element.getResources().get(index), writer);
			}
			writer.append(")");
		}
		writer.append(" ");
		this.blockPrinter.print(element.getBlock(), writer);
		for (CatchBlock cat : element.getCatchBlocks()) {
			this.catchBlockPrinter.print(cat, writer);
		}
		if (element.getFinallyBlock() != null) {
			writer.append("finally ");
			this.blockPrinter.print(element.getFinallyBlock(), writer);
		}
	}

}
