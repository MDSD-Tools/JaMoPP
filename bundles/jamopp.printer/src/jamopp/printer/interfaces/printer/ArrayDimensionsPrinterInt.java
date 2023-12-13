package jamopp.printer.interfaces.printer;

import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.ArrayDimensionsPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(ArrayDimensionsPrinter.class)
public interface ArrayDimensionsPrinterInt extends Printer<List<ArrayDimension>> {

}