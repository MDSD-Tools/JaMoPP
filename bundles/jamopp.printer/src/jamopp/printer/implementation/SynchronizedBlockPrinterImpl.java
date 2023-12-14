package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.SynchronizedBlock;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.SynchronizedBlockPrinterInt;

public class SynchronizedBlockPrinterImpl implements SynchronizedBlockPrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;
	private final BlockPrinterInt BlockPrinter;

	@Inject
	public SynchronizedBlockPrinterImpl(ExpressionPrinterInt expressionPrinter, BlockPrinterInt blockPrinter) {
		ExpressionPrinter = expressionPrinter;
		BlockPrinter = blockPrinter;
	}

	@Override
	public void print(SynchronizedBlock element, BufferedWriter writer) throws IOException {
		writer.append("synchronized (");
		ExpressionPrinter.print(element.getLockProvider(), writer);
		writer.append(") ");
		BlockPrinter.print(element.getBlock(), writer);
	}

}
