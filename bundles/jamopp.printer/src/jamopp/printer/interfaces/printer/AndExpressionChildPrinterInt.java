package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.AndExpressionChild;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.AndExpressionChildPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(AndExpressionChildPrinter.class)
public interface AndExpressionChildPrinterInt extends Printer<AndExpressionChild> {

}