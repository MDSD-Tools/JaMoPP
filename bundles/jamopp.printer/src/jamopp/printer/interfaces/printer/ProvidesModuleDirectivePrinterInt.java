package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modules.ProvidesModuleDirective;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ProvidesModuleDirectivePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ProvidesModuleDirectivePrinter.class)
public interface ProvidesModuleDirectivePrinterInt extends Printer<ProvidesModuleDirective> {

}