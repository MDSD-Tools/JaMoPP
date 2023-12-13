package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.DefaultSwitchCase;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.DefaultSwitchCasePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(DefaultSwitchCasePrinter.class)
public interface DefaultSwitchCasePrinterInt extends Printer<DefaultSwitchCase> {

}