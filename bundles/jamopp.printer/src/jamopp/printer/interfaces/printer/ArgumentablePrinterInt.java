package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.Argumentable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ArgumentablePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ArgumentablePrinter.class)
public interface ArgumentablePrinterInt extends Printer<Argumentable> {

}