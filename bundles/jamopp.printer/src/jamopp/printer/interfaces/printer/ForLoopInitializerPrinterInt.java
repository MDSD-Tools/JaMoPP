package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.ForLoopInitializer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ForLoopInitializerPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ForLoopInitializerPrinter.class)
public interface ForLoopInitializerPrinterInt extends Printer<ForLoopInitializer> {

}