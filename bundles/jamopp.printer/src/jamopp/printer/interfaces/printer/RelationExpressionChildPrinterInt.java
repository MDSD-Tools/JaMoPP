package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.RelationExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.RelationExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(RelationExpressionChildPrinter.class)
public interface RelationExpressionChildPrinterInt extends Printer<RelationExpressionChild> {

}