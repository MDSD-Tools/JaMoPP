package jamopp.printer.interfaces.printer;

import org.emftext.language.java.arrays.ArraySelector;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ArraySelectorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ArraySelectorPrinter.class)
public interface ArraySelectorPrinterInt extends Printer<ArraySelector> {

}