package jamopp.printer.interfaces.printer;

import org.emftext.language.java.operators.RelationOperator;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.RelationOperatorPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(RelationOperatorPrinter.class)
public interface RelationOperatorPrinterInt extends Printer<RelationOperator> {

}