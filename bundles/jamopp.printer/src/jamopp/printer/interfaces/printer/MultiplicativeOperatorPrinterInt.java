package jamopp.printer.interfaces.printer;

import org.emftext.language.java.operators.MultiplicativeOperator;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MultiplicativeOperatorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(MultiplicativeOperatorPrinter.class)
public interface MultiplicativeOperatorPrinterInt extends Printer<MultiplicativeOperator> {

}