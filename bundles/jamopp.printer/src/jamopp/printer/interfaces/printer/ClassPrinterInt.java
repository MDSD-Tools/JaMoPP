package jamopp.printer.interfaces.printer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ClassPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ClassPrinter.class)
public interface ClassPrinterInt extends Printer<org.emftext.language.java.classifiers.Class> {

}