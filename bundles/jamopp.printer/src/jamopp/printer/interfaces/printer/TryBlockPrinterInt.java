package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.TryBlock;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.TryBlockPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(TryBlockPrinter.class)
public interface TryBlockPrinterInt extends Printer<TryBlock> {

}