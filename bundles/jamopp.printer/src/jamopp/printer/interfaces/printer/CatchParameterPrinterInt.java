package jamopp.printer.interfaces.printer;

import org.emftext.language.java.parameters.CatchParameter;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.CatchParameterPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(CatchParameterPrinter.class)
public interface CatchParameterPrinterInt extends Printer<CatchParameter> {

}