package jamopp.printer.interfaces.printer;

import org.emftext.language.java.annotations.Annotable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AnnotablePrinter;
import jamopp.printer.interfaces.Printer;


public interface AnnotablePrinterInt extends Printer<Annotable> {

}