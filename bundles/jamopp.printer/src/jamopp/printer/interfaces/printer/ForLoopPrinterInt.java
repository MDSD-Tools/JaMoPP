package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.ForLoop;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ForLoopPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ForLoopPrinter.class)
public interface ForLoopPrinterInt extends Printer<ForLoop> {

}