package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Continue;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ContinuePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ContinuePrinter.class)
public interface ContinuePrinterInt extends Printer<Continue> {

}