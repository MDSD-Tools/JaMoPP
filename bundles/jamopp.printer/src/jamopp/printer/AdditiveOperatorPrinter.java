package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Addition;
import org.emftext.language.java.operators.AdditiveOperator;

public class AdditiveOperatorPrinter {

	static void printAdditiveOperator(AdditiveOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Addition) {
			writer.append(" + ");
		} else {
			writer.append(" - ");
		}
	}

}
