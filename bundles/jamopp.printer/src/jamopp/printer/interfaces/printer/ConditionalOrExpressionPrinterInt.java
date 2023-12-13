package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ConditionalOrExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConditionalOrExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ConditionalOrExpressionPrinter.class)
public interface ConditionalOrExpressionPrinterInt extends Printer<ConditionalOrExpression> {

}