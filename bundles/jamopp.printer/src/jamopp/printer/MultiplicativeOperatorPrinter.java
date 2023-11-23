package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Division;
import org.emftext.language.java.operators.Multiplication;
import org.emftext.language.java.operators.MultiplicativeOperator;

public class MultiplicativeOperatorPrinter {

	static void printMultiplicativeOperator(MultiplicativeOperator element, BufferedWriter writer)
			throws IOException {
		if (element instanceof Multiplication) {
			writer.append(" * ");
		} else if (element instanceof Division) {
			writer.append(" / ");
		} else {
			writer.append(" % ");
		}
	}

}
