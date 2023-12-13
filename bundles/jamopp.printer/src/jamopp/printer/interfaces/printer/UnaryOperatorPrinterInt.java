package jamopp.printer.interfaces.printer;

import org.emftext.language.java.operators.UnaryOperator;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.UnaryOperatorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(UnaryOperatorPrinter.class)
public interface UnaryOperatorPrinterInt extends Printer<UnaryOperator> {

}