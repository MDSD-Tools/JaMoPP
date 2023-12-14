package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.ExpressionStatement;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ExpressionStatementPrinter;
import jamopp.printer.interfaces.Printer;


public interface ExpressionStatementPrinterInt extends Printer<ExpressionStatement> {

}