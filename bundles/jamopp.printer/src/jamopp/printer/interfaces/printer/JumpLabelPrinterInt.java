package jamopp.printer.interfaces.printer;

import org.emftext.language.java.statements.JumpLabel;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.JumpLabelPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(JumpLabelPrinter.class)
public interface JumpLabelPrinterInt extends Printer<JumpLabel> {

}