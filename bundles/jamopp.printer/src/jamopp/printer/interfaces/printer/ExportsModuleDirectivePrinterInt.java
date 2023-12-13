package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modules.ExportsModuleDirective;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ExportsModuleDirectivePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ExportsModuleDirectivePrinter.class)
public interface ExportsModuleDirectivePrinterInt extends Printer<ExportsModuleDirective> {

}