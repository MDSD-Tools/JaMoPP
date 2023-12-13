package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modules.AccessProvidingModuleDirective;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.RemainingAccessProvidingModuleDirectivePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(RemainingAccessProvidingModuleDirectivePrinter.class)
public interface RemainingAccessProvidingModuleDirectivePrinterInt extends Printer<AccessProvidingModuleDirective> {

}