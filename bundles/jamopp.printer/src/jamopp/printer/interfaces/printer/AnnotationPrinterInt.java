package jamopp.printer.interfaces.printer;

import org.emftext.language.java.classifiers.Annotation;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AnnotationPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AnnotationPrinter.class)
public interface AnnotationPrinterInt extends Printer<Annotation> {

}