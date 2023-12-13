package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.MethodReferenceExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MethodReferenceExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(MethodReferenceExpressionPrinter.class)
public interface MethodReferenceExpressionPrinterInt extends Printer<MethodReferenceExpression> {

}