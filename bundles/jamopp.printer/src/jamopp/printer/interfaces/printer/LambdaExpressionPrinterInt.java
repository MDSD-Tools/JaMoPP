package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.LambdaExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.LambdaExpressionPrinter;
import jamopp.printer.interfaces.Printer;


public interface LambdaExpressionPrinterInt extends Printer<LambdaExpression> {

}