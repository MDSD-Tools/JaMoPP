package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.EnumConstant;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.EnumConstantPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(EnumConstantPrinter.class)
public interface EnumConstantPrinterInt extends Printer<EnumConstant> {

}