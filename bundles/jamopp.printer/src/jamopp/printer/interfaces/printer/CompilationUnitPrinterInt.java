package jamopp.printer.interfaces.printer;

import org.emftext.language.java.containers.CompilationUnit;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.CompilationUnitPrinter;
import jamopp.printer.interfaces.Printer;


public interface CompilationUnitPrinterInt extends Printer<CompilationUnit> {

}