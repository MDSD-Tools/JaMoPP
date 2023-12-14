package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.SynchronizedBlock;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.SynchronizedBlockPrinterInt;

public class SynchronizedBlockPrinterImpl implements SynchronizedBlockPrinterInt {

	private final Printer<Block> BlockPrinter;
	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public SynchronizedBlockPrinterImpl(Printer<Expression> expressionPrinter, Printer<Block> blockPrinter) {
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
