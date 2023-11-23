package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.CatchBlock;
import org.emftext.language.java.statements.TryBlock;

public class TryBlockPrinter {

	static void printTryBlock(TryBlock element, BufferedWriter writer) throws IOException {
		writer.append("try");
		if (!element.getResources().isEmpty()) {
			writer.append("(");
			ResourcePrinter.printResource(element.getResources().get(0), writer);
			for (int index = 1; index < element.getResources().size(); index++) {
				writer.append("; ");
				ResourcePrinter.printResource(element.getResources().get(index), writer);
			}
			writer.append(")");
		}
		writer.append(" ");
		BlockPrinter.printBlock(element.getBlock(), writer);
		for (CatchBlock cat : element.getCatchBlocks()) {
			CatchBlockPrinter.printCatchBlock(cat, writer);
		}
		if (element.getFinallyBlock() != null) {
			writer.append("finally ");
			BlockPrinter.printBlock(element.getFinallyBlock(), writer);
		}
	}

}
