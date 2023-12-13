package jamopp.printer.interfaces.printer;

import org.emftext.language.java.arrays.ArrayInstantiation;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ArrayInstantiationPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ArrayInstantiationPrinter.class)
public interface ArrayInstantiationPrinterInt extends Printer<ArrayInstantiation> {

}