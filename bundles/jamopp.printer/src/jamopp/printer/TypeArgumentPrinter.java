package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.UnknownTypeArgument;

public class TypeArgumentPrinter {

	static void printTypeArgument(TypeArgument element, BufferedWriter writer) throws IOException {
		if (element instanceof QualifiedTypeArgument arg) {
			TypeReferencePrinter.printTypeReference(arg.getTypeReference(), writer);
		} else if (element instanceof UnknownTypeArgument arg) {
			AnnotablePrinter.printAnnotable(arg, writer);
			writer.append("?");
		} else if (element instanceof SuperTypeArgument arg) {
			AnnotablePrinter.printAnnotable(arg, writer);
			writer.append("? super ");
			TypeReferencePrinter.printTypeReference(arg.getSuperType(), writer);
		} else {
			ExtendsTypeArgument arg = (ExtendsTypeArgument) element;
			AnnotablePrinter.printAnnotable(arg, writer);
			writer.append("? extends ");
			TypeReferencePrinter.printTypeReference(arg.getExtendType(), writer);
		}
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
	}

}
