package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.AssignmentExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AssignmentExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AssignmentExpressionChildPrinter.class)
public interface AssignmentExpressionChildPrinterInt extends Printer<AssignmentExpressionChild> {

}