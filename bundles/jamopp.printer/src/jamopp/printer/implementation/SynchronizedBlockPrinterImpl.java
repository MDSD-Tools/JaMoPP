package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.SynchronizedBlock;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class SynchronizedBlockPrinterImpl implements Printer<SynchronizedBlock> {

	private final Printer<Block> blockPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public SynchronizedBlockPrinterImpl(Printer<Expression> expressionPrinter, Printer<Block> blockPrinter) {
		this.expressionPrinter = expressionPrinter;
		this.blockPrinter = blockPrinter;
	}

	@Override
	public void print(SynchronizedBlock element, BufferedWriter writer) throws IOException {
		writer.append("synchronized (");
		this.expressionPrinter.print(element.getLockProvider(), writer);
		writer.append(") ");
		this.blockPrinter.print(element.getBlock(), writer);
	}

}
