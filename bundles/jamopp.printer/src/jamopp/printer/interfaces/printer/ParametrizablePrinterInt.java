package jamopp.printer.interfaces.printer;

import org.emftext.language.java.parameters.Parametrizable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ParametrizablePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ParametrizablePrinter.class)
public interface ParametrizablePrinterInt extends Printer<Parametrizable> {

}