package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.UnaryExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.UnaryExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(UnaryExpressionChildPrinter.class)
public interface UnaryExpressionChildPrinterInt extends Printer<UnaryExpressionChild> {

}