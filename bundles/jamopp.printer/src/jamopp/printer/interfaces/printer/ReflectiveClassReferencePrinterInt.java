package jamopp.printer.interfaces.printer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ReflectiveClassReferencePrinter;
import jamopp.printer.interfaces.EmptyPrinter;

@ImplementedBy(ReflectiveClassReferencePrinter.class)
public interface ReflectiveClassReferencePrinterInt extends EmptyPrinter {

}