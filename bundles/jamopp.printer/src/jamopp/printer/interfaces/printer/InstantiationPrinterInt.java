package jamopp.printer.interfaces.printer;

import org.emftext.language.java.instantiations.Instantiation;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InstantiationPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(InstantiationPrinter.class)
public interface InstantiationPrinterInt extends Printer<Instantiation> {

}