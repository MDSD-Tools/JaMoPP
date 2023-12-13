package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ShiftExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ShiftExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ShiftExpressionPrinter.class)
public interface ShiftExpressionPrinterInt extends Printer<ShiftExpression> {

}