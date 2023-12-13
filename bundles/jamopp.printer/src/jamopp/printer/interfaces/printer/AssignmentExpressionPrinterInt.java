package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.AssignmentExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AssignmentExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AssignmentExpressionPrinter.class)
public interface AssignmentExpressionPrinterInt extends Printer<AssignmentExpression> {

}