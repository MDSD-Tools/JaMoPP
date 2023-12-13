package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.InclusiveOrExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InclusiveOrExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(InclusiveOrExpressionPrinter.class)
public interface InclusiveOrExpressionPrinterInt extends Printer<InclusiveOrExpression> {

}