package jamopp.printer.interfaces.printer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.EmptyMemberPrinter;
import jamopp.printer.interfaces.EmptyPrinter;

@ImplementedBy(EmptyMemberPrinter.class)
public interface EmptyMemberPrinterInt extends EmptyPrinter {

}