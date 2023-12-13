package jamopp.printer.interfaces.printer;

import org.emftext.language.java.containers.JavaRoot;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.JavaRootPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(JavaRootPrinter.class)
public interface JavaRootPrinterInt extends Printer<JavaRoot> {

}
