package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.Expression;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ExpressionPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ExpressionPrinter.class)
public interface ExpressionPrinterInt extends Printer<Expression> {

}