package jamopp.printer.interfaces.printer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ModulePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ModulePrinter.class)
public interface ModulePrinterInt extends Printer<org.emftext.language.java.containers.Module> {

}