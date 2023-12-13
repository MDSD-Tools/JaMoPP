package jamopp.printer.interfaces.printer;

import org.emftext.language.java.types.ClassifierReference;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ClassifierReferencePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ClassifierReferencePrinter.class)
public interface ClassifierReferencePrinterInt extends Printer<ClassifierReference> {

}