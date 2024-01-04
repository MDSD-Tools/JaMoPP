package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ImplicitlyTypedLambdaParameters;
import tools.mdsd.jamopp.model.java.expressions.LambdaParameters;
import tools.mdsd.jamopp.model.java.expressions.SingleImplicitLambdaParameter;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
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
			handleSingleImplicitLambdaParameter(element, writer);
		} else if (element instanceof ImplicitlyTypedLambdaParameters) {
			handleImplicitlyTypedLambdaParameters(element, writer);
		} else {
			handleOther(element, writer);
		}
		writer.append(")");
	}

	private void handleOther(LambdaParameters element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (var index = 0; index < element.getParameters().size(); index++) {
			var param = element.getParameters().get(index);
			if (param instanceof OrdinaryParameter) {
				ordinaryParameterPrinter.print((OrdinaryParameter) param, writer);
			} else {
				variableLengthParameterPrinter.print((VariableLengthParameter) param, writer);
			}
			if (index < element.getParameters().size() - 1) {
				writer.append(", ");
			}
		}
	}

	private void handleSingleImplicitLambdaParameter(LambdaParameters element, BufferedWriter writer)
			throws IOException {
		writer.append(element.getParameters().get(0).getName());
	}

	private void handleImplicitlyTypedLambdaParameters(LambdaParameters element, BufferedWriter writer)
			throws IOException {
		writer.append("(");
		for (var index = 0; index < element.getParameters().size(); index++) {
			writer.append(element.getParameters().get(index).getName());
			if (index < element.getParameters().size() - 1) {
				writer.append(", ");
			}
		}
	}

}
