package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.ForEachLoop;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ForEachLoopPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ForEachLoopPrinter.class)
public interface ForEachLoopPrinterInt extends Printer<ForEachLoop> {

}