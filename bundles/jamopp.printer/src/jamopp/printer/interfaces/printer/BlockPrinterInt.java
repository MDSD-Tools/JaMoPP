package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Block;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.BlockPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(BlockPrinter.class)
public interface BlockPrinterInt extends Printer<Block> {

}