package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.SynchronizedBlock;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class SynchronizedBlockPrinter implements Printer<SynchronizedBlock> {

	private final ExpressionPrinter ExpressionPrinter;
	private final BlockPrinter BlockPrinter;

	@Inject
	public SynchronizedBlockPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter,
			jamopp.printer.implementation.BlockPrinter blockPrinter) {
		super();
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
