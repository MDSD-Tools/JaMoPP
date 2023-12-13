package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.NestedExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.NestedExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(NestedExpressionPrinter.class)
public interface NestedExpressionPrinterInt extends Printer<NestedExpression> {

}