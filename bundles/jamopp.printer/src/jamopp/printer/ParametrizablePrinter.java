package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.Parametrizable;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.parameters.VariableLengthParameter;

public class ParametrizablePrinter {

	static void printParametrizable(Parametrizable element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (int index = 0; index < element.getParameters().size(); index++) {
			Parameter param = element.getParameters().get(index);
			if (param instanceof ReceiverParameter) {
				ReceiverParameterPrinter.printReceiverParameter((ReceiverParameter) param, writer);
			} else if (param instanceof OrdinaryParameter) {
				OrdinaryParameterPrinter.printOrdinaryParameter((OrdinaryParameter) param, writer);
			} else {
				VariableLengthParameterPrinter.printVariableLengthParameter((VariableLengthParameter) param, writer);
			}
			if (index < element.getParameters().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")");
	}

}
