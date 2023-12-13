package jamopp.printer.interfaces.printer;

import org.emftext.language.java.types.PrimitiveType;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.PrimitiveTypePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(PrimitiveTypePrinter.class)
public interface PrimitiveTypePrinterInt extends Printer<PrimitiveType> {

}