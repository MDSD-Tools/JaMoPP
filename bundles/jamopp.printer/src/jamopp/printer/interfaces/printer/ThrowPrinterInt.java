package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Throw;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ThrowPrinter;
import jamopp.printer.interfaces.Printer;


public interface ThrowPrinterInt extends Printer<Throw> {

}