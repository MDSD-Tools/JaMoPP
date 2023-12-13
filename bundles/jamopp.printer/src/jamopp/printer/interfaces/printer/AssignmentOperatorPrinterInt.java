package jamopp.printer.interfaces.printer;

import org.emftext.language.java.operators.AssignmentOperator;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AssignmentOperatorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AssignmentOperatorPrinter.class)
public interface AssignmentOperatorPrinterInt extends Printer<AssignmentOperator> {

}