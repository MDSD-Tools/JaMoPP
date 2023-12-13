package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.ExceptionThrower;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ExceptionThrowerPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ExceptionThrowerPrinter.class)
public interface ExceptionThrowerPrinterInt extends Printer<ExceptionThrower> {

}