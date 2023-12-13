package jamopp.printer.interfaces.printer;

import org.emftext.language.java.references.MethodCall;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MethodCallPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(MethodCallPrinter.class)
public interface MethodCallPrinterInt extends Printer<MethodCall> {

}