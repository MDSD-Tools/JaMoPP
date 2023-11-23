package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Addition;
import org.emftext.language.java.operators.Negate;
import org.emftext.language.java.operators.Subtraction;
import org.emftext.language.java.operators.UnaryOperator;

public class UnaryOperatorPrinter {

	static void printUnaryOperator(UnaryOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Addition) {
			writer.append("+");
		} else if (element instanceof Subtraction) {
			writer.append("-");
		} else if (element instanceof Negate) {
			writer.append("!");
		} else {
			writer.append("~");
		}
	}

}
