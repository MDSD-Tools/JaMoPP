package jamopp.printer.interfaces.printer;

import org.emftext.language.java.generics.TypeArgument;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.TypeArgumentPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(TypeArgumentPrinter.class)
public interface TypeArgumentPrinterInt extends Printer<TypeArgument> {

}