package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AnnotationInstanceOrModifierPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AnnotationInstanceOrModifierPrinter.class)
public interface AnnotationInstanceOrModifierPrinterInt extends Printer<AnnotationInstanceOrModifier> {

}