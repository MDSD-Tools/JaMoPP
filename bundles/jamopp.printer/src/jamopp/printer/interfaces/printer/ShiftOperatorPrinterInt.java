package jamopp.printer.interfaces.printer;

import org.emftext.language.java.operators.ShiftOperator;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ShiftOperatorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ShiftOperatorPrinter.class)
public interface ShiftOperatorPrinterInt extends Printer<ShiftOperator> {

}