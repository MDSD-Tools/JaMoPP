package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ExclusiveOrExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ExclusiveOrExpressionChildPrinter.class)
public interface ExclusiveOrExpressionChildPrinterInt extends Printer<ExclusiveOrExpressionChild> {

}