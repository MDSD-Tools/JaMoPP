package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.UnknownTypeArgument;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.printer.interfaces.Printer;

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
