package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.PrefixUnaryModificationExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(PrefixUnaryModificationExpressionPrinter.class)
public interface PrefixUnaryModificationExpressionPrinterInt extends Printer<PrefixUnaryModificationExpression> {

}