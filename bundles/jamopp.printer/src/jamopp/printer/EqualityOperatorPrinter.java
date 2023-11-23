package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Equal;
import org.emftext.language.java.operators.EqualityOperator;
import org.emftext.language.java.operators.NotEqual;

public class EqualityOperatorPrinter {

	static void printEqualityOperator(EqualityOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Equal) {
			writer.append(" == ");
		} else if (element instanceof NotEqual) {
			writer.append(" != ");
		}
	}

}
