package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.NormalSwitchCase;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.NormalSwitchCasePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(NormalSwitchCasePrinter.class)
public interface NormalSwitchCasePrinterInt extends Printer<NormalSwitchCase> {

}