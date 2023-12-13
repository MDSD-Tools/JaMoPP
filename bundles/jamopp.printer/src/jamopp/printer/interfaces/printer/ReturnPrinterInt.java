package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Return;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ReturnPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ReturnPrinter.class)
public interface ReturnPrinterInt extends Printer<Return> {

}