package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.Parametrizable;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.OrdinaryParameterPrinterInt;
import jamopp.printer.interfaces.printer.ParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.ReceiverParameterPrinterInt;
import jamopp.printer.interfaces.printer.VariableLengthParameterPrinterInt;

public class ParametrizablePrinterImpl implements ParametrizablePrinterInt {

	private final ReceiverParameterPrinterInt ReceiverParameterPrinter;
	private final OrdinaryParameterPrinterInt OrdinaryParameterPrinter;
	private final VariableLengthParameterPrinterInt VariableLengthParameterPrinter;

	@Inject
	public ParametrizablePrinterImpl(ReceiverParameterPrinterInt receiverParameterPrinter,
			OrdinaryParameterPrinterInt ordinaryParameterPrinter,
			VariableLengthParameterPrinterInt variableLengthParameterPrinter) {
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
