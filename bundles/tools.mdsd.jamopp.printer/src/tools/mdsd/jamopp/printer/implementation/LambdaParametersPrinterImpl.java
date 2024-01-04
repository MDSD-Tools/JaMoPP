package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.ImplicitlyTypedLambdaParameters;
import tools.mdsd.jamopp.model.java.expressions.LambdaParameters;
import tools.mdsd.jamopp.model.java.expressions.SingleImplicitLambdaParameter;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class LambdaParametersPrinterImpl implements Printer<LambdaParameters> {

	private final Printer<OrdinaryParameter> ordinaryParameterPrinter;
	private final Printer<VariableLengthParameter> variableLengthParameterPrinter;

	@Inject
	public LambdaParametersPrinterImpl(Printer<OrdinaryParameter> ordinaryParameterPrinter,
			Printer<VariableLengthParameter> variableLengthParameterPrinter) {
		this.ordinaryParameterPrinter = ordinaryParameterPrinter;
		this.variableLengthParameterPrinter = variableLengthParameterPrinter;
	}

	@Override
	public void print(LambdaParameters element, BufferedWriter writer) throws IOException {
		if (element instanceof SingleImplicitLambdaParameter) {
			writer.append(element.getParameters().get(0).getName());
		} else {
			if (element instanceof ImplicitlyTypedLambdaParameters) {
				writer.append("(");
				for (var index = 0; index < element.getParameters().size(); index++) {
					writer.append(element.getParameters().get(index).getName());
					if (index < element.getParameters().size() - 1) {
						writer.append(", ");
					}
				}
			} else {
				writer.append("(");
				for (var index = 0; index < element.getParameters().size(); index++) {
					var param = element.getParameters().get(index);
					if (param instanceof OrdinaryParameter) {
						this.ordinaryParameterPrinter.print((OrdinaryParameter) param, writer);
					} else {
						this.variableLengthParameterPrinter.print((VariableLengthParameter) param, writer);
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
