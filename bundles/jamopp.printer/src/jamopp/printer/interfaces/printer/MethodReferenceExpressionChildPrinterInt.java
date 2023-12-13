package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MethodReferenceExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(MethodReferenceExpressionChildPrinter.class)
public interface MethodReferenceExpressionChildPrinterInt extends Printer<MethodReferenceExpressionChild> {

}