package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.SynchronizedBlock;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.SynchronizedBlockPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(SynchronizedBlockPrinter.class)
public interface SynchronizedBlockPrinterInt extends Printer<SynchronizedBlock> {

}