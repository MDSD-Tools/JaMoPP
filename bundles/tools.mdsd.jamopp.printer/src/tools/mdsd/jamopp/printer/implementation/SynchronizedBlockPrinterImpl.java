package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.SynchronizedBlock;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SynchronizedBlockPrinterImpl implements Printer<SynchronizedBlock> {

	private final Printer<Block> blockPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public SynchronizedBlockPrinterImpl(final Printer<Expression> expressionPrinter,
			final Printer<Block> blockPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.blockPrinter = blockPrinter;
	}

	@Override
	public void print(final SynchronizedBlock element, final BufferedWriter writer) throws IOException {
		writer.append("synchronized (");
		expressionPrinter.print(element.getLockProvider(), writer);
		writer.append(") ");
		blockPrinter.print(element.getBlock(), writer);
	}

}
