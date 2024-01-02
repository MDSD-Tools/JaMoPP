package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.generics.ExtendsTypeArgument;
import tools.mdsd.jamopp.model.java.generics.QualifiedTypeArgument;
import tools.mdsd.jamopp.model.java.generics.SuperTypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.generics.UnknownTypeArgument;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import javax.inject.Inject;
import javax.inject.Provider;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TypeArgumentPrinterImpl implements Printer<TypeArgument> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Provider<Printer<TypeReference>> typeReferencePrinter;

	@Inject
	public TypeArgumentPrinterImpl(Provider<Printer<TypeReference>> typeReferencePrinter,
			Printer<Annotable> annotablePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
		this.annotablePrinter = annotablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(TypeArgument element, BufferedWriter writer) throws IOException {
		if (element instanceof QualifiedTypeArgument arg) {
			this.typeReferencePrinter.get().print(arg.getTypeReference(), writer);
		} else if (element instanceof UnknownTypeArgument arg) {
			this.annotablePrinter.print(arg, writer);
			writer.append("?");
		} else if (element instanceof SuperTypeArgument arg) {
			this.annotablePrinter.print(arg, writer);
			writer.append("? super ");
			this.typeReferencePrinter.get().print(arg.getSuperType(), writer);
		} else {
			var arg = (ExtendsTypeArgument) element;
			this.annotablePrinter.print(arg, writer);
			writer.append("? extends ");
			this.typeReferencePrinter.get().print(arg.getExtendType(), writer);
		}
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
