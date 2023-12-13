package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.UnknownTypeArgument;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class TypeArgumentPrinter implements Printer<TypeArgument> {

	private final TypeReferencePrinter TypeReferencePrinter;
	private final AnnotablePrinter AnnotablePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;

	@Inject
	public TypeArgumentPrinter(jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.AnnotablePrinter annotablePrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter) {
		super();
		TypeReferencePrinter = typeReferencePrinter;
		AnnotablePrinter = annotablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(TypeArgument element, BufferedWriter writer) throws IOException {
		if (element instanceof QualifiedTypeArgument arg) {
			TypeReferencePrinter.print(arg.getTypeReference(), writer);
		} else if (element instanceof UnknownTypeArgument arg) {
			AnnotablePrinter.print(arg, writer);
			writer.append("?");
		} else if (element instanceof SuperTypeArgument arg) {
			AnnotablePrinter.print(arg, writer);
			writer.append("? super ");
			TypeReferencePrinter.print(arg.getSuperType(), writer);
		} else {
			ExtendsTypeArgument arg = (ExtendsTypeArgument) element;
			AnnotablePrinter.print(arg, writer);
			writer.append("? extends ");
			TypeReferencePrinter.print(arg.getExtendType(), writer);
		}
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
