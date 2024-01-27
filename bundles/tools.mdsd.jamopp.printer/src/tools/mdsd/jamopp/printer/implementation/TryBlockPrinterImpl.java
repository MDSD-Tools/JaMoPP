package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.CatchBlock;
import tools.mdsd.jamopp.model.java.statements.TryBlock;
import tools.mdsd.jamopp.model.java.variables.Resource;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TryBlockPrinterImpl implements Printer<TryBlock> {

	private final Printer<Block> blockPrinter;
	private final Printer<CatchBlock> catchBlockPrinter;
	private final Printer<Resource> resourcePrinter;

	@Inject
	public TryBlockPrinterImpl(final Printer<Resource> resourcePrinter, final Printer<Block> blockPrinter,
			final Printer<CatchBlock> catchBlockPrinter) {
		this.resourcePrinter = resourcePrinter;
		this.blockPrinter = blockPrinter;
		this.catchBlockPrinter = catchBlockPrinter;
	}

	@Override
	public void print(final TryBlock element, final BufferedWriter writer) throws IOException {
		writer.append("try");
		if (!element.getResources().isEmpty()) {
			writer.append("(");
			resourcePrinter.print(element.getResources().get(0), writer);
			for (var index = 1; index < element.getResources().size(); index++) {
				writer.append("; ");
				resourcePrinter.print(element.getResources().get(index), writer);
			}
			writer.append(")");
		}
		writer.append(" ");
		blockPrinter.print(element.getBlock(), writer);
		for (final CatchBlock cat : element.getCatchBlocks()) {
			catchBlockPrinter.print(cat, writer);
		}
		if (element.getFinallyBlock() != null) {
			writer.append("finally ");
			blockPrinter.print(element.getFinallyBlock(), writer);
		}
	}

}
