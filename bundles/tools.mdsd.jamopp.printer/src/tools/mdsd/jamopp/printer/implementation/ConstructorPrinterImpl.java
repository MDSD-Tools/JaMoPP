package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.generics.TypeParametrizable;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.ExceptionThrower;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConstructorPrinterImpl implements Printer<Constructor> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<Block> blockPrinter;
	private final Printer<ExceptionThrower> exceptionThrowerPrinter;
	private final Printer<Parametrizable> parametrizablePrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;

	@Inject
	public ConstructorPrinterImpl(final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			final Printer<TypeParametrizable> typeParametrizablePrinter,
			final Printer<Parametrizable> parametrizablePrinter,
			final Printer<ExceptionThrower> exceptionThrowerPrinter, final Printer<Block> blockPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.parametrizablePrinter = parametrizablePrinter;
		this.exceptionThrowerPrinter = exceptionThrowerPrinter;
		this.blockPrinter = blockPrinter;
	}

	@Override
	public void print(final Constructor element, final BufferedWriter writer) throws IOException {
		annotableAndModifiablePrinter.print(element, writer);
		typeParametrizablePrinter.print(element, writer);
		writer.append(" " + element.getName());
		parametrizablePrinter.print(element, writer);
		exceptionThrowerPrinter.print(element, writer);
		blockPrinter.print(element.getBlock(), writer);
	}

}
