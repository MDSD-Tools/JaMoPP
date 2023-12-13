package jamopp.printer.interfaces.printer;

import org.emftext.language.java.operators.UnaryModificationOperator;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.UnaryModificationOperatorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(UnaryModificationOperatorPrinter.class)
public interface UnaryModificationOperatorPrinterInt extends Printer<UnaryModificationOperator> {

}