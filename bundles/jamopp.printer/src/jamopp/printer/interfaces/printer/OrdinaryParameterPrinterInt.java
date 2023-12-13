package jamopp.printer.interfaces.printer;

import org.emftext.language.java.parameters.OrdinaryParameter;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.OrdinaryParameterPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(OrdinaryParameterPrinter.class)
public interface OrdinaryParameterPrinterInt extends Printer<OrdinaryParameter> {

}