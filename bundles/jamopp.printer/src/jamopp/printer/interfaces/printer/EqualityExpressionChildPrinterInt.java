package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.EqualityExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.EqualityExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(EqualityExpressionChildPrinter.class)
public interface EqualityExpressionChildPrinterInt extends Printer<EqualityExpressionChild> {

}