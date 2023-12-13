package jamopp.printer.interfaces.printer;

import org.emftext.language.java.classifiers.ConcreteClassifier;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConcreteClassifierPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ConcreteClassifierPrinter.class)
public interface ConcreteClassifierPrinterInt extends Printer<ConcreteClassifier> {

}