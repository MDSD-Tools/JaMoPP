package jamopp.printer.interfaces.printer;

import org.emftext.language.java.classifiers.Implementor;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ImplementorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ImplementorPrinter.class)
public interface ImplementorPrinterInt extends Printer<Implementor> {

}