package jamopp.printer.interfaces.printer;

import org.emftext.language.java.classifiers.AnonymousClass;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AnonymousClassPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AnonymousClassPrinter.class)
public interface AnonymousClassPrinterInt extends Printer<AnonymousClass> {

}