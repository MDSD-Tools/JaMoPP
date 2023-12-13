package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.ElementReference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ElementReferencePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ElementReferencePrinter.class)
public interface ElementReferencePrinterInt extends Printer<ElementReference> {

}