package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.SelfReference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.SelfReferencePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(SelfReferencePrinter.class)
public interface SelfReferencePrinterInt extends Printer<SelfReference> {

}