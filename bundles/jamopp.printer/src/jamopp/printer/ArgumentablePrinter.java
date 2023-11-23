package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.Argumentable;

public class ArgumentablePrinter {

	static void printArgumentable(Argumentable element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (int index = 0; index < element.getArguments().size(); index++) {
			ExpressionPrinter.printExpression(element.getArguments().get(index), writer);
			if (index < element.getArguments().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")");
	}

}
