package jamopp.printer.interfaces.printer;

import org.emftext.language.java.arrays.ArrayInitializer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ArrayInitializerPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ArrayInitializerPrinter.class)
public interface ArrayInitializerPrinterInt extends Printer<ArrayInitializer> {

}