package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.ClassMethod;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ClassMethodPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ClassMethodPrinter.class)
public interface ClassMethodPrinterInt extends Printer<ClassMethod> {

}