package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Constructor;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.BlockPrinterInt;
import jamopp.printer.interfaces.printer.ConstructorPrinterInt;
import jamopp.printer.interfaces.printer.ExceptionThrowerPrinterInt;
import jamopp.printer.interfaces.printer.ParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.TypeParametrizablePrinterInt;

public class ConstructorPrinter implements ConstructorPrinterInt {

	private final AnnotableAndModifiablePrinterInt AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinterInt TypeParametrizablePrinter;
	private final ParametrizablePrinterInt ParametrizablePrinter;
	private final ExceptionThrowerPrinterInt ExceptionThrowerPrinter;
	private final BlockPrinterInt BlockPrinter;

	@Inject
	public ConstructorPrinter(AnnotableAndModifiablePrinterInt annotableAndModifiablePrinter,
			TypeParametrizablePrinterInt typeParametrizablePrinter, ParametrizablePrinterInt parametrizablePrinter,
			ExceptionThrowerPrinterInt exceptionThrowerPrinter, BlockPrinterInt blockPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeParametrizablePrinter = typeParametrizablePrinter;
		ParametrizablePrinter = parametrizablePrinter;
		ExceptionThrowerPrinter = exceptionThrowerPrinter;
		BlockPrinter = blockPrinter;
	}

	@Override
	public void print(Constructor element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeParametrizablePrinter.print(element, writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.print(element, writer);
		ExceptionThrowerPrinter.print(element, writer);
		BlockPrinter.print(element.getBlock(), writer);
	}

}
