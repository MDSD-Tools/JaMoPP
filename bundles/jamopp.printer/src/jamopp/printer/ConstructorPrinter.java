package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.Constructor;

class ConstructorPrinter {

	static void print(Constructor element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeParametrizablePrinter.print(element, writer);
		writer.append(" " + element.getName());
		ParametrizablePrinter.print(element, writer);
		ExceptionThrowerPrinter.print(element, writer);
		BlockPrinter.print(element.getBlock(), writer);
	}

}
