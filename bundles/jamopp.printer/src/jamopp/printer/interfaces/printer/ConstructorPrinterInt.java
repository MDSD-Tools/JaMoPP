package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.Constructor;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConstructorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ConstructorPrinter.class)
public interface ConstructorPrinterInt extends Printer<Constructor> {

}