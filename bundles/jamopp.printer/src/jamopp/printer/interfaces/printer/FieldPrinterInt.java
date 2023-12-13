package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.Field;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.FieldPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(FieldPrinter.class)
public interface FieldPrinterInt extends Printer<Field> {

}