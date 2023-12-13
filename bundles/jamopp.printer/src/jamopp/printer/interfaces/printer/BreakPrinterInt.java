package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Break;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.BreakPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(BreakPrinter.class)
public interface BreakPrinterInt extends Printer<Break> {

}