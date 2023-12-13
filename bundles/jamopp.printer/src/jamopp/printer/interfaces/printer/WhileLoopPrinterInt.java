package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.WhileLoop;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.WhileLoopPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(WhileLoopPrinter.class)
public interface WhileLoopPrinterInt extends Printer<WhileLoop> {

}