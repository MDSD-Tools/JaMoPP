package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.NormalSwitchRule;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.NormalSwitchRulePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(NormalSwitchRulePrinter.class)
public interface NormalSwitchRulePrinterInt extends Printer<NormalSwitchRule> {

}