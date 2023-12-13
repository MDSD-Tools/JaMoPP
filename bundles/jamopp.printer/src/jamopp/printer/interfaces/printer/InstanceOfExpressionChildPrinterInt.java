package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.InstanceOfExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InstanceOfExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(InstanceOfExpressionChildPrinter.class)
public interface InstanceOfExpressionChildPrinterInt extends Printer<InstanceOfExpressionChild> {

}