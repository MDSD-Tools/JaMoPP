package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.EqualityExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.EqualityExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(EqualityExpressionPrinter.class)
public interface EqualityExpressionPrinterInt extends Printer<EqualityExpression> {

}