package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.AdditiveExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AdditiveExpressionPrinter;
import jamopp.printer.interfaces.Printer;


public interface AdditiveExpressionPrinterInt extends Printer<AdditiveExpression> {
}