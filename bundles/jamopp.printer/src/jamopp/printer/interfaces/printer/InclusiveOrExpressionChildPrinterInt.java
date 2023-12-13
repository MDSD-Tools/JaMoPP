package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InclusiveOrExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(InclusiveOrExpressionChildPrinter.class)
public interface InclusiveOrExpressionChildPrinterInt extends Printer<InclusiveOrExpressionChild> {

}