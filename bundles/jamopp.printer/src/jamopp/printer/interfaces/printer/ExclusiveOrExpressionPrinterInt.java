package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ExclusiveOrExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ExclusiveOrExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ExclusiveOrExpressionPrinter.class)
public interface ExclusiveOrExpressionPrinterInt extends Printer<ExclusiveOrExpression> {

}