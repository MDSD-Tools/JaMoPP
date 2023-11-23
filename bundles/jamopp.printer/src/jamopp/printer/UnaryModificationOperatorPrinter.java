package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.PlusPlus;
import org.emftext.language.java.operators.UnaryModificationOperator;

class UnaryModificationOperatorPrinter {

	static void print(UnaryModificationOperator element, BufferedWriter writer)
			throws IOException {
		if (element instanceof PlusPlus) {
			writer.append("++");
		} else {
			writer.append("--");
		}
	}

}
