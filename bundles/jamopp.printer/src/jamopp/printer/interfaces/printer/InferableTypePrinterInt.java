package jamopp.printer.interfaces.printer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InferableTypePrinter;
import jamopp.printer.interfaces.EmptyPrinter;

@ImplementedBy(InferableTypePrinter.class)
public interface InferableTypePrinterInt extends EmptyPrinter {

}