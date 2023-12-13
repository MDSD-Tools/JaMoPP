package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.AdditiveExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AdditiveExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AdditiveExpressionChildPrinter.class)
public interface AdditiveExpressionChildPrinterInt extends Printer<AdditiveExpressionChild> {

}