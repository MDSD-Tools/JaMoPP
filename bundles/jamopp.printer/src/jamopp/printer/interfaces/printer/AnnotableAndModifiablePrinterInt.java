package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modifiers.AnnotableAndModifiable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AnnotableAndModifiablePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AnnotableAndModifiablePrinter.class)
public interface AnnotableAndModifiablePrinterInt extends Printer<AnnotableAndModifiable> {

}