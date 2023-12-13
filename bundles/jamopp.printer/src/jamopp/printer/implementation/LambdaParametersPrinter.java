package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ImplicitlyTypedLambdaParameters;
import org.emftext.language.java.expressions.LambdaParameters;
import org.emftext.language.java.expressions.SingleImplicitLambdaParameter;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class LambdaParametersPrinter implements Printer<LambdaParameters> {

	private final OrdinaryParameterPrinter OrdinaryParameterPrinter;
	private final VariableLengthParameterPrinter VariableLengthParameterPrinter;

	@Inject
	public LambdaParametersPrinter(jamopp.printer.implementation.OrdinaryParameterPrinter ordinaryParameterPrinter,
			jamopp.printer.implementation.VariableLengthParameterPrinter variableLengthParameterPrinter) {
		super();
		OrdinaryParameterPrinter = ordinaryParameterPrinter;
		VariableLengthParameterPrinter = variableLengthParameterPrinter;
	}

	@Override
	public void print(LambdaParameters element, BufferedWriter writer) throws IOException {
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
						OrdinaryParameterPrinter.print((OrdinaryParameter) param, writer);
					} else {
						VariableLengthParameterPrinter.print((VariableLengthParameter) param, writer);
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
