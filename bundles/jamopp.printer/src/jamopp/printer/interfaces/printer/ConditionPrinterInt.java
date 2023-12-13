package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.Condition;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ConditionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ConditionPrinter.class)
public interface ConditionPrinterInt extends Printer<Condition> {

}