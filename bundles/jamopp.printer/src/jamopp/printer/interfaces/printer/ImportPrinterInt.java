package jamopp.printer.interfaces.printer;

import org.emftext.language.java.imports.Import;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ImportPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ImportPrinter.class)
public interface ImportPrinterInt extends Printer<Import> {

}