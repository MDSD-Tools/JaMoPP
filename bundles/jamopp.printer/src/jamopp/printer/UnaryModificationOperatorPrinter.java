package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.PlusPlus;
import org.emftext.language.java.operators.UnaryModificationOperator;

public class UnaryModificationOperatorPrinter {

	static void printUnaryModificationOperator(UnaryModificationOperator element, BufferedWriter writer)
			throws IOException {
		if (element instanceof PlusPlus) {
			writer.append("++");
		} else {
			writer.append("--");
		}
	}

}
