package jamopp.printer.interfaces.printer;

import org.emftext.language.java.modifiers.Modifier;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ModifierPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ModifierPrinter.class)
public interface ModifierPrinterInt extends Printer<Modifier> {

}