package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.SynchronizedBlock;

class SynchronizedBlockPrinter {

	static void print(SynchronizedBlock element, BufferedWriter writer) throws IOException {
		writer.append("synchronized (");
		ExpressionPrinter.print(element.getLockProvider(), writer);
		writer.append(") ");
		BlockPrinter.print(element.getBlock(), writer);
	}

}
