package jamopp.printer.interfaces.printer;

import org.emftext.language.java.expressions.LambdaParameters;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.LambdaParametersPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(LambdaParametersPrinter.class)
public interface LambdaParametersPrinterInt extends Printer<LambdaParameters> {

}