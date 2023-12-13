package jamopp.printer.interfaces.printer;

import org.emftext.language.java.parameters.ReceiverParameter;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ReceiverParameterPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ReceiverParameterPrinter.class)
public interface ReceiverParameterPrinterInt extends Printer<ReceiverParameter> {

}