package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.SwitchCase;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.SwitchCasePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(SwitchCasePrinter.class)
public interface SwitchCasePrinterInt extends Printer<SwitchCase> {

}