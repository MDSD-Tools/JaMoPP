package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ImplicitlyTypedLambdaParameters;
import org.emftext.language.java.expressions.LambdaParameters;
import org.emftext.language.java.expressions.SingleImplicitLambdaParameter;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.VariableLengthParameter;

public class LambdaParametersPrinter {

	static void printLambdaParameters(LambdaParameters element, BufferedWriter writer) throws IOException {
		if (element instanceof SingleImplicitLambdaParameter) {
			writer.append(element.getParameters().get(0).getName());
		} else {
			if (element instanceof ImplicitlyTypedLambdaParameters) {
				writer.append("(");
				for (int index = 0; index < element.getParameters().size(); index++) {
					writer.append(element.getParameters().get(index).getName());
					if (index < element.getParameters().size() - 1) {
						writer.append(", ");
					}
				}
			} else {
				writer.append("(");
				for (int index = 0; index < element.getParameters().size(); index++) {
					Parameter param = element.getParameters().get(index);
					if (param instanceof OrdinaryParameter) {
						OrdinaryParameterPrinter.printOrdinaryParameter((OrdinaryParameter) param, writer);
					} else {
						VariableLengthParameterPrinter.printVariableLengthParameter((VariableLengthParameter) param, writer);
					}
					if (index < element.getParameters().size() - 1) {
						writer.append(", ");
					}
				}
			}
			writer.append(")");
		}
	}

}
