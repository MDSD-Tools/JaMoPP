package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ConditionalExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConditionalExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ConditionalExpressionChildPrinter.class)
public interface ConditionalExpressionChildPrinterInt extends Printer<ConditionalExpressionChild> {

}