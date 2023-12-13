package jamopp.printer.interfaces.printer;

import org.emftext.language.java.literals.Literal;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.LiteralPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(LiteralPrinter.class)
public interface LiteralPrinterInt extends Printer<Literal> {

}