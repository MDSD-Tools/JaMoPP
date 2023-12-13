package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.LocalVariableStatement;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.LocalVariableStatementPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(LocalVariableStatementPrinter.class)
public interface LocalVariableStatementPrinterInt extends Printer<LocalVariableStatement> {

}