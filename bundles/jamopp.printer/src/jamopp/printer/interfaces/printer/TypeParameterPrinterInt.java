package jamopp.printer.interfaces.printer;

import org.emftext.language.java.generics.TypeParameter;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.TypeParameterPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(TypeParameterPrinter.class)
public interface TypeParameterPrinterInt extends Printer<TypeParameter> {

}