package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.YieldStatement;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.YieldStatementPrinter;
import jamopp.printer.interfaces.Printer;


public interface YieldStatementPrinterInt extends Printer<YieldStatement> {

}