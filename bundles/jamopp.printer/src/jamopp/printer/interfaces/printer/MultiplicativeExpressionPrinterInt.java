package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.MultiplicativeExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MultiplicativeExpressionPrinter;
import jamopp.printer.interfaces.Printer;


public interface MultiplicativeExpressionPrinterInt extends Printer<MultiplicativeExpression> {

}