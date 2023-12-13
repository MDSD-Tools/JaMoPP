package jamopp.printer.interfaces.printer;

import org.emftext.language.java.variables.Resource;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ResourcePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ResourcePrinter.class)
public interface ResourcePrinterInt extends Printer<Resource> {

}