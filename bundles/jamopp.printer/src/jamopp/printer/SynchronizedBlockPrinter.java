package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.SynchronizedBlock;

public class SynchronizedBlockPrinter {

	static void printSynchronizedBlock(SynchronizedBlock element, BufferedWriter writer) throws IOException {
		writer.append("synchronized (");
		ExpressionPrinter.printExpression(element.getLockProvider(), writer);
		writer.append(") ");
		BlockPrinter.printBlock(element.getBlock(), writer);
	}

}
