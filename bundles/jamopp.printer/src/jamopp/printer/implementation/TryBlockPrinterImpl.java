package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.CatchBlock;
import org.emftext.language.java.statements.TryBlock;
import org.emftext.language.java.variables.Resource;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class TryBlockPrinterImpl implements Printer<TryBlock> {

	private final Printer<Block> BlockPrinter;
	private final Printer<CatchBlock> CatchBlockPrinter;
	private final Printer<Resource> ResourcePrinter;

	@Inject
	public TryBlockPrinterImpl(Printer<Resource> resourcePrinter, Printer<Block> blockPrinter,
			Printer<CatchBlock> catchBlockPrinter) {
		ResourcePrinter = resourcePrinter;
		BlockPrinter = blockPrinter;
		CatchBlockPrinter = catchBlockPrinter;
	}

	@Override
	public void print(TryBlock element, BufferedWriter writer) throws IOException {
		writer.append("try");
		if (!element.getResources().isEmpty()) {
			writer.append("(");
			ResourcePrinter.print(element.getResources().get(0), writer);
			for (var index = 1; index < element.getResources().size(); index++) {
				writer.append("; ");
				ResourcePrinter.print(element.getResources().get(index), writer);
			}
			writer.append(")");
		}
		writer.append(" ");
		BlockPrinter.print(element.getBlock(), writer);
		for (CatchBlock cat : element.getCatchBlocks()) {
			CatchBlockPrinter.print(cat, writer);
		}
		if (element.getFinallyBlock() != null) {
			writer.append("finally ");
			BlockPrinter.print(element.getFinallyBlock(), writer);
		}
	}

}
