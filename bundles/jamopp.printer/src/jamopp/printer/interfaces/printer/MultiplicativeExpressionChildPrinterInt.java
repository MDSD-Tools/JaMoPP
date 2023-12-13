package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MultiplicativeExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(MultiplicativeExpressionChildPrinter.class)
public interface MultiplicativeExpressionChildPrinterInt extends Printer<MultiplicativeExpressionChild> {

}