package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modules.UsesModuleDirective;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.UsesModuleDirectivePrinter;
import jamopp.printer.interfaces.Printer;


public interface UsesModuleDirectivePrinterInt extends Printer<UsesModuleDirective> {

}