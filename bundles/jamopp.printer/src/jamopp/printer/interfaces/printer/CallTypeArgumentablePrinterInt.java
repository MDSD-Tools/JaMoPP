package jamopp.printer.interfaces.printer;

import org.emftext.language.java.generics.CallTypeArgumentable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.CallTypeArgumentablePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(CallTypeArgumentablePrinter.class)
public interface CallTypeArgumentablePrinterInt extends Printer<CallTypeArgumentable> {

}