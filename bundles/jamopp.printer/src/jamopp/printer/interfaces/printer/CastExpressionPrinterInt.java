package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.CastExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.CastExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(CastExpressionPrinter.class)
public interface CastExpressionPrinterInt extends Printer<CastExpression> {

}