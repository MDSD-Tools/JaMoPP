package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.InstanceOfExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InstanceOfExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(InstanceOfExpressionPrinter.class)
public interface InstanceOfExpressionPrinterInt extends Printer<InstanceOfExpression> {

}