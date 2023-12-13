package jamopp.printer.interfaces.printer;

import org.emftext.language.java.types.NamespaceClassifierReference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.NamespaceClassifierReferencePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(NamespaceClassifierReferencePrinter.class)
public interface NamespaceClassifierReferencePrinterInt extends Printer<NamespaceClassifierReference> {

}