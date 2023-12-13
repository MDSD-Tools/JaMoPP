package jamopp.printer.interfaces.printer;

import org.emftext.language.java.annotations.AnnotationValue;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AnnotationValuePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AnnotationValuePrinter.class)
public interface AnnotationValuePrinterInt extends Printer<AnnotationValue> {

}