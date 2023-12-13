package jamopp.printer.interfaces.printer;

import org.emftext.language.java.literals.Self;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.SelfPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(SelfPrinter.class)
public interface SelfPrinterInt extends Printer<Self> {

}