package jamopp.printer.interfaces.printer;

import org.emftext.language.java.classifiers.Interface;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.InterfacePrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(InterfacePrinter.class)
public 
interface InterfacePrinterInt  extends Printer<Interface>{

}