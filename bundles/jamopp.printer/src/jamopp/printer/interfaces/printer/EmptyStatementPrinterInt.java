package jamopp.printer.interfaces.printer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.EmptyStatementPrinter;
import jamopp.printer.interfaces.EmptyPrinter;

@ImplementedBy(EmptyStatementPrinter.class)
public interface EmptyStatementPrinterInt extends EmptyPrinter {

}