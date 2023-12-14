package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modules.ProvidesModuleDirective;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ProvidesModuleDirectivePrinter;
import jamopp.printer.interfaces.Printer;


public interface ProvidesModuleDirectivePrinterInt extends Printer<ProvidesModuleDirective> {

}