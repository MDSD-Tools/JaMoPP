package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.SuffixUnaryModificationExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(SuffixUnaryModificationExpressionPrinter.class)
public interface SuffixUnaryModificationExpressionPrinterInt extends Printer<SuffixUnaryModificationExpression> {

}