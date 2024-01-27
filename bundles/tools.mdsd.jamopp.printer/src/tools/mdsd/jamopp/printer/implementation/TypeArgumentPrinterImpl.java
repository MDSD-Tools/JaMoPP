package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.generics.ExtendsTypeArgument;
import tools.mdsd.jamopp.model.java.generics.QualifiedTypeArgument;
import tools.mdsd.jamopp.model.java.generics.SuperTypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.generics.UnknownTypeArgument;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TypeArgumentPrinterImpl implements Printer<TypeArgument> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Provider<Printer<TypeReference>> typeReferencePrinter;

	@Inject
	public TypeArgumentPrinterImpl(final Provider<Printer<TypeReference>> typeReferencePrinter,
			final Printer<Annotable> annotablePrinter, final Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
		this.annotablePrinter = annotablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(final TypeArgument element, final BufferedWriter writer) throws IOException {
		if (element instanceof final QualifiedTypeArgument arg) {
			typeReferencePrinter.get().print(arg.getTypeReference(), writer);
		} else if (element instanceof final UnknownTypeArgument arg) {
			annotablePrinter.print(arg, writer);
			writer.append("?");
		} else if (element instanceof final SuperTypeArgument arg) {
			annotablePrinter.print(arg, writer);
			writer.append("? super ");
			typeReferencePrinter.get().print(arg.getSuperType(), writer);
		} else {
			final var arg = (ExtendsTypeArgument) element;
			annotablePrinter.print(arg, writer);
			writer.append("? extends ");
			typeReferencePrinter.get().print(arg.getExtendType(), writer);
		}
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
