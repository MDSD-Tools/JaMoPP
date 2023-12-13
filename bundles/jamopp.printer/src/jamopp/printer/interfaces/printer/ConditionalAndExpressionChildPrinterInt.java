package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConditionalAndExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ConditionalAndExpressionChildPrinter.class)
public interface ConditionalAndExpressionChildPrinterInt extends Printer<ConditionalAndExpressionChild> {

}