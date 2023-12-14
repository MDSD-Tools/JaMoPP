package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ConditionalExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConditionalExpressionPrinter;
import jamopp.printer.interfaces.Printer;


public interface ConditionalExpressionPrinterInt extends Printer<ConditionalExpression> {

}