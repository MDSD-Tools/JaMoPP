package jamopp.printer.interfaces.printer;

import org.emftext.language.java.generics.TypeParametrizable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.TypeParametrizablePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(TypeParametrizablePrinter.class)
public interface TypeParametrizablePrinterInt extends Printer<TypeParametrizable> {

}