package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.AndExpression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AndExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AndExpressionPrinter.class)
public interface AndExpressionPrinterInt extends Printer<AndExpression> {

}