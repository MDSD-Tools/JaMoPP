package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.RelationExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.RelationExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(RelationExpressionPrinter.class)
public interface RelationExpressionPrinterInt extends Printer<RelationExpression> {

}