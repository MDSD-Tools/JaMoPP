package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modules.OpensModuleDirective;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.OpensModuleDirectivePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(OpensModuleDirectivePrinter.class)
public interface OpensModuleDirectivePrinterInt extends Printer<OpensModuleDirective> {

}