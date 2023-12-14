package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.CatchBlock;
import org.emftext.language.java.statements.TryBlock;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.CatchBlockPrinterInt;
import jamopp.printer.interfaces.printer.ResourcePrinterInt;
import jamopp.printer.interfaces.printer.TryBlockPrinterInt;

public class TryBlockPrinter implements TryBlockPrinterInt {

	private final ResourcePrinterInt ResourcePrinter;
	private final BlockPrinterInt BlockPrinter;
	private final CatchBlockPrinterInt CatchBlockPrinter;

	@Inject
	public TryBlockPrinter(ResourcePrinterInt resourcePrinter, BlockPrinterInt blockPrinter,
			CatchBlockPrinterInt catchBlockPrinter) {
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
			for (int index = 1; index < element.getResources().size(); index++) {
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
