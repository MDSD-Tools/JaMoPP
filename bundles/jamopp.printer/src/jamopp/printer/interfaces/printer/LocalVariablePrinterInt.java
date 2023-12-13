package jamopp.printer.interfaces.printer;

import org.emftext.language.java.variables.LocalVariable;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.LocalVariablePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(LocalVariablePrinter.class)
public interface LocalVariablePrinterInt extends Printer<LocalVariable> {

}