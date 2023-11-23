package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.LeftShift;
import org.emftext.language.java.operators.RightShift;
import org.emftext.language.java.operators.ShiftOperator;

public class ShiftOperatorPrinter {

	static void printShiftOperator(ShiftOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof LeftShift) {
			writer.append(" << ");
		} else if (element instanceof RightShift) {
			writer.append(" >> ");
		} else {
			writer.append(" >>> ");
		}
	}

}