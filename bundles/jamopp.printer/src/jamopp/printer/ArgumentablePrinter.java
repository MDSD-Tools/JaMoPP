package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.Argumentable;

class ArgumentablePrinter {

	static void print(Argumentable element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (int index = 0; index < element.getArguments().size(); index++) {
			ExpressionPrinter.print(element.getArguments().get(index), writer);
			if (index < element.getArguments().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")");
	}

}
