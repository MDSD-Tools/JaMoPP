package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.statements.CatchBlock;

public class CatchBlockPrinter {

	static void printCatchBlock(CatchBlock element, BufferedWriter writer) throws IOException {
		writer.append("catch(");
		CatchParameterPrinter.printCatchParameter((CatchParameter) element.getParameter(), writer);
		writer.append(")");
		BlockPrinter.printBlock(element.getBlock(), writer);
	}

}
