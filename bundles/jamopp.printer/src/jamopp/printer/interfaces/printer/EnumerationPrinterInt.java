package jamopp.printer.interfaces.printer;

import org.emftext.language.java.classifiers.Enumeration;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.EnumerationPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(EnumerationPrinter.class)
public interface EnumerationPrinterInt extends Printer<Enumeration> {

}