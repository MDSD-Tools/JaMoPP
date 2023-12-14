package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.UnaryExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.UnaryExpressionPrinter;
import jamopp.printer.interfaces.Printer;


public interface UnaryExpressionPrinterInt extends Printer<UnaryExpression> {

}