package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.generics.TypeParametrizable;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.ExceptionThrower;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.parameters.Parametrizable;
import tools.mdsd.jamopp.model.java.statements.Block;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConstructorPrinterImpl implements Printer<Constructor> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<Block> blockPrinter;
	private final Printer<ExceptionThrower> exceptionThrowerPrinter;
	private final Printer<Parametrizable> parametrizablePrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;

	@Inject
	public ConstructorPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeParametrizable> typeParametrizablePrinter, Printer<Parametrizable> parametrizablePrinter,
			Printer<ExceptionThrower> exceptionThrowerPrinter, Printer<Block> blockPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.parametrizablePrinter = parametrizablePrinter;
		this.exceptionThrowerPrinter = exceptionThrowerPrinter;
		this.blockPrinter = blockPrinter;
	}

	@Override
	public void print(Constructor element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		this.typeParametrizablePrinter.print(element, writer);
		writer.append(" " + element.getName());
		this.parametrizablePrinter.print(element, writer);
		this.exceptionThrowerPrinter.print(element, writer);
		this.blockPrinter.print(element.getBlock(), writer);
	}

}
