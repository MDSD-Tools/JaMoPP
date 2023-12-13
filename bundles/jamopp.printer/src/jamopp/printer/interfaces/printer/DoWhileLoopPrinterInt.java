package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.DoWhileLoop;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.DoWhileLoopPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(DoWhileLoopPrinter.class)
public interface DoWhileLoopPrinterInt extends Printer<DoWhileLoop> {

}