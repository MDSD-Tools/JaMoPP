package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Constructor;

import jamopp.printer.interfaces.Printer;

class ConstructorPrinter implements Printer<Constructor>{

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinter TypeParametrizablePrinter;
	private final ParametrizablePrinter ParametrizablePrinter;
	private final ExceptionThrowerPrinter ExceptionThrowerPrinter;
	private final BlockPrinter BlockPrinter;
	
	public void print(Constructor element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeParametrizablePrinter.print(element, writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.print(element, writer);
		ExceptionThrowerPrinter.print(element, writer);
		BlockPrinter.print(element.getBlock(), writer);
	}

}
