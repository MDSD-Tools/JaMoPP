package jamopp.printer.interfaces.printer;

import org.emftext.language.java.annotations.AnnotationInstance;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AnnotationInstancePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AnnotationInstancePrinter.class)
public interface AnnotationInstancePrinterInt extends Printer<AnnotationInstance> {

}