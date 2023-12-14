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

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentPrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class TypeArgumentPrinterImpl implements TypeArgumentPrinterInt {

	private final Provider<TypeReferencePrinterInt> TypeReferencePrinter;
	private final Printer<Annotable> AnnotablePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;

	@Inject
	public TypeArgumentPrinterImpl(Provider<TypeReferencePrinterInt> typeReferencePrinter,
			Printer<Annotable> annotablePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		TypeReferencePrinter = typeReferencePrinter;
		AnnotablePrinter = annotablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(TypeArgument element, BufferedWriter writer) throws IOException {
		if (element instanceof QualifiedTypeArgument arg) {
			TypeReferencePrinter.get().print(arg.getTypeReference(), writer);
		} else if (element instanceof UnknownTypeArgument arg) {
			AnnotablePrinter.print(arg, writer);
			writer.append("?");
		} else if (element instanceof SuperTypeArgument arg) {
			AnnotablePrinter.print(arg, writer);
			writer.append("? super ");
			TypeReferencePrinter.get().print(arg.getSuperType(), writer);
		} else {
			ExtendsTypeArgument arg = (ExtendsTypeArgument) element;
			AnnotablePrinter.print(arg, writer);
			writer.append("? extends ");
			TypeReferencePrinter.get().print(arg.getExtendType(), writer);
		}
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
