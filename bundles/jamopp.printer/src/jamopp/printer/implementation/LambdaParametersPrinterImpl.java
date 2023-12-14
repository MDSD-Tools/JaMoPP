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


import jamopp.printer.interfaces.printer.LambdaParametersPrinterInt;
import jamopp.printer.interfaces.printer.OrdinaryParameterPrinterInt;
import jamopp.printer.interfaces.printer.VariableLengthParameterPrinterInt;

public class LambdaParametersPrinterImpl implements LambdaParametersPrinterInt {

	private final OrdinaryParameterPrinterInt OrdinaryParameterPrinter;
	private final VariableLengthParameterPrinterInt VariableLengthParameterPrinter;

	@Inject
	public LambdaParametersPrinterImpl(OrdinaryParameterPrinterInt ordinaryParameterPrinter,
			VariableLengthParameterPrinterInt variableLengthParameterPrinter) {
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
