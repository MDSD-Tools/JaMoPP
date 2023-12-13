package jamopp.printer.interfaces.printer;

import org.emftext.language.java.generics.TypeArgumentable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.TypeArgumentablePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(TypeArgumentablePrinter.class)
public interface TypeArgumentablePrinterInt extends Printer<TypeArgumentable> {

}