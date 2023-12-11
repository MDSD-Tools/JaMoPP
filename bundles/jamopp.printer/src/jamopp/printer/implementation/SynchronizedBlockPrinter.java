package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.SynchronizedBlock;

import jamopp.printer.interfaces.Printer;

class SynchronizedBlockPrinter implements Printer<SynchronizedBlock>{

	public void print(SynchronizedBlock element, BufferedWriter writer) throws IOException {
		writer.append("synchronized (");
		ExpressionPrinter.print(element.getLockProvider(), writer);
		writer.append(") ");
		BlockPrinter.print(element.getBlock(), writer);
	}

}