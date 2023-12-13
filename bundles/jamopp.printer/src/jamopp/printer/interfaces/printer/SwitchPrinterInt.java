package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Switch;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.SwitchPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(SwitchPrinter.class)
public interface SwitchPrinterInt extends Printer<Switch> {

}