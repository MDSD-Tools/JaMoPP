package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.Reference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ReferencePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ReferencePrinter.class)
public interface ReferencePrinterInt extends Printer<Reference> {

}