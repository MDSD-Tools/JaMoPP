package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Statement;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.StatementPrinter;
import jamopp.printer.interfaces.Printer;


public interface StatementPrinterInt extends Printer<Statement> {

}