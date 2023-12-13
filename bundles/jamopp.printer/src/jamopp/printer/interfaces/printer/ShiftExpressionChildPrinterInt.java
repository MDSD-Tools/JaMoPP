package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ShiftExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ShiftExpressionChildPrinter.class)
public interface ShiftExpressionChildPrinterInt extends Printer<ShiftExpressionChild> {

}