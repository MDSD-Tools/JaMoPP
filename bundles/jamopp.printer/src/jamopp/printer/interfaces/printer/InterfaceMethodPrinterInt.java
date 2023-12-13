package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.InterfaceMethod;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InterfaceMethodPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(InterfaceMethodPrinter.class)
public interface InterfaceMethodPrinterInt extends Printer<InterfaceMethod> {

}