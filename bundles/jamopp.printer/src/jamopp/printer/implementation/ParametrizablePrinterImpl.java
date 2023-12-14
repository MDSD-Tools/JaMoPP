package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parametrizable;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ParametrizablePrinterImpl implements Printer<Parametrizable> {

	private final Printer<OrdinaryParameter> OrdinaryParameterPrinter;
	private final Printer<ReceiverParameter> ReceiverParameterPrinter;
	private final Printer<VariableLengthParameter> VariableLengthParameterPrinter;

	@Inject
	public ParametrizablePrinterImpl(Printer<ReceiverParameter> receiverParameterPrinter,
			Printer<OrdinaryParameter> ordinaryParameterPrinter,
			Printer<VariableLengthParameter> variableLengthParameterPrinter) {
		ReceiverParameterPrinter = receiverParameterPrinter;
		OrdinaryParameterPrinter = ordinaryParameterPrinter;
		VariableLengthParameterPrinter = variableLengthParameterPrinter;
	}

	@Override
	public void print(Parametrizable element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (var index = 0; index < element.getParameters().size(); index++) {
			var param = element.getParameters().get(index);
			if (param instanceof ReceiverParameter) {
				ReceiverParameterPrinter.print((ReceiverParameter) param, writer);
			} else if (param instanceof OrdinaryParameter) {
				OrdinaryParameterPrinter.print((OrdinaryParameter) param, writer);
			} else {
				VariableLengthParameterPrinter.print((VariableLengthParameter) param, writer);
			}
			if (index < element.getParameters().size() - 1) {
				writer.append(", ");
			}
		}
		writer.append(")");
	}

}
