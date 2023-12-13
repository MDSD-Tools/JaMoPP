package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Assert;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AssertPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AssertPrinter.class)
public interface AssertPrinterInt extends Printer<Assert> {

}