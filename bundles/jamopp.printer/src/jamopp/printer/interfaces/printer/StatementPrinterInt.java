package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Statement;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.StatementPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(StatementPrinter.class)
public interface StatementPrinterInt extends Printer<Statement> {

}