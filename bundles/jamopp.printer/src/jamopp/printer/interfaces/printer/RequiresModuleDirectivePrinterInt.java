package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modules.RequiresModuleDirective;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.RequiresModuleDirectivePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(RequiresModuleDirectivePrinter.class)
public interface RequiresModuleDirectivePrinterInt extends Printer<RequiresModuleDirective> {

}