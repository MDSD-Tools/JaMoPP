package jamopp.printer.interfaces.printer;

import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.VariableLengthParameterPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(VariableLengthParameterPrinter.class)
public interface VariableLengthParameterPrinterInt extends Printer<VariableLengthParameter> {

}