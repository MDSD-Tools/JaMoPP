package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.ExceptionThrower;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.statements.Block;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.TypeParametrizablePrinterInt;

public class ConstructorPrinterImpl implements Printer<Constructor> {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<Block> BlockPrinter;
	private final Printer<ExceptionThrower> ExceptionThrowerPrinter;
	private final ParametrizablePrinterInt ParametrizablePrinter;
	private final TypeParametrizablePrinterInt TypeParametrizablePrinter;

	@Inject
	public ConstructorPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			TypeParametrizablePrinterInt typeParametrizablePrinter, ParametrizablePrinterInt parametrizablePrinter,
			Printer<ExceptionThrower> exceptionThrowerPrinter, Printer<Block> blockPrinter) {
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
