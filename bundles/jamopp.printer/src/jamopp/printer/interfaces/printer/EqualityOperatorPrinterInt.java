package jamopp.printer.interfaces.printer;

import org.emftext.language.java.operators.EqualityOperator;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.EqualityOperatorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(EqualityOperatorPrinter.class)
public interface EqualityOperatorPrinterInt extends Printer<EqualityOperator> {

}