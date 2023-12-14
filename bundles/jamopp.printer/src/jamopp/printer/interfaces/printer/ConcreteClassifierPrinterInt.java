package jamopp.printer.interfaces.printer;

import org.emftext.language.java.classifiers.ConcreteClassifier;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConcreteClassifierPrinter;
import jamopp.printer.interfaces.Printer;


public interface ConcreteClassifierPrinterInt extends Printer<ConcreteClassifier> {

}