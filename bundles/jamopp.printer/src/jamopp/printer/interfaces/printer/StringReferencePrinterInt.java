package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.StringReference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.StringReferencePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(StringReferencePrinter.class)
public interface StringReferencePrinterInt extends Printer<StringReference> {

}