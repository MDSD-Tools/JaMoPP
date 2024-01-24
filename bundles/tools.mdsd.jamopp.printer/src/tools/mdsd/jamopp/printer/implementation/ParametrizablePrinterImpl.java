package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ParametrizablePrinterImpl implements Printer<Parametrizable> {

	private final Printer<OrdinaryParameter> ordinaryParameterPrinter;
	private final Printer<ReceiverParameter> receiverParameterPrinter;
	private final Printer<VariableLengthParameter> variableLengthParameterPrinter;

	@Inject
	public ParametrizablePrinterImpl(final Printer<ReceiverParameter> receiverParameterPrinter,
			final Printer<OrdinaryParameter> ordinaryParameterPrinter,
			final Printer<VariableLengthParameter> variableLengthParameterPrinter) {
		this.receiverParameterPrinter = receiverParameterPrinter;
		this.ordinaryParameterPrinter = ordinaryParameterPrinter;
		this.variableLengthParameterPrinter = variableLengthParameterPrinter;
	}

	@Override
	public void print(final Parametrizable element, final BufferedWriter writer) throws IOException {
		writer.append("(");
		for (var index = 0; index < element.getParameters().size(); index++) {
			final var param = element.getParameters().get(index);
			if (param instanceof ReceiverParameter) {
				receiverParameterPrinter.print((ReceiverParameter) param, writer);
			} else if (param instanceof OrdinaryParameter) {
				ordinaryParameterPrinter.print((OrdinaryParameter) param, writer);
			} else {
				variableLengthParameterPrinter.print((VariableLengthParameter) param, writer);
			}
			if (index < element.getParameters().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")");
	}

}
