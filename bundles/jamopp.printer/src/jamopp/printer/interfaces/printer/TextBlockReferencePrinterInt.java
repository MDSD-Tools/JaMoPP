package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.TextBlockReference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.TextBlockReferencePrinter;
import jamopp.printer.interfaces.Printer;


public interface TextBlockReferencePrinterInt extends Printer<TextBlockReference> {

}