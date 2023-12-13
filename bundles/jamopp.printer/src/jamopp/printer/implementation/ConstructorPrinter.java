package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Constructor;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConstructorPrinterInt;

class ConstructorPrinter implements Printer<Constructor>, ConstructorPrinterInt {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinter TypeParametrizablePrinter;
	private final ParametrizablePrinter ParametrizablePrinter;
	private final ExceptionThrowerPrinter ExceptionThrowerPrinter;
	private final BlockPrinter BlockPrinter;

	@Inject
	public ConstructorPrinter(jamopp.printer.implementation.AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeParametrizablePrinter typeParametrizablePrinter,
			jamopp.printer.implementation.ParametrizablePrinter parametrizablePrinter,
			jamopp.printer.implementation.ExceptionThrowerPrinter exceptionThrowerPrinter,
			jamopp.printer.implementation.BlockPrinter blockPrinter) {
		super();
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
