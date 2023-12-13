package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.Parametrizable;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ParametrizablePrinterInt;

public class ParametrizablePrinter implements ParametrizablePrinterInt {

	private final ReceiverParameterPrinter ReceiverParameterPrinter;
	private final OrdinaryParameterPrinter OrdinaryParameterPrinter;
	private final VariableLengthParameterPrinter VariableLengthParameterPrinter;

	@Inject
	public ParametrizablePrinter(jamopp.printer.implementation.ReceiverParameterPrinter receiverParameterPrinter,
			jamopp.printer.implementation.OrdinaryParameterPrinter ordinaryParameterPrinter,
			jamopp.printer.implementation.VariableLengthParameterPrinter variableLengthParameterPrinter) {
		super();
		ReceiverParameterPrinter = receiverParameterPrinter;
		OrdinaryParameterPrinter = ordinaryParameterPrinter;
		VariableLengthParameterPrinter = variableLengthParameterPrinter;
	}

	@Override
	public void print(Parametrizable element, BufferedWriter writer) throws IOException {
		writer.append("(");
		for (int index = 0; index < element.getParameters().size(); index++) {
			Parameter param = element.getParameters().get(index);
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
