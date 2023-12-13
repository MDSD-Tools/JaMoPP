package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.PrimitiveTypeReference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.PrimitiveTypeReferencePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(PrimitiveTypeReferencePrinter.class)
public interface PrimitiveTypeReferencePrinterInt extends Printer<PrimitiveTypeReference> {

}