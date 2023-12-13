package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.CatchBlock;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.CatchBlockPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(CatchBlockPrinter.class)
public interface CatchBlockPrinterInt extends Printer<CatchBlock> {

}